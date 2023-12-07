package ru.kslacker.fintech.cache;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;

@Component
@Aspect
/**
 * Аспект используется для кэширования результатов запросов в CityWeatherService
 */
public class CachingAspect {
    private final WeatherCacheRepository cache;

    public CachingAspect(WeatherCacheRepository cache) {
        this.cache = cache;
    }

    @Pointcut("within(ru.kslacker.fintech.service.api.CityWeatherService+)")
    protected void weatherServicePointcut() {

    }

    @Pointcut("weatherServicePointcut() && execution(* getWeather(..))")
    protected void getWeatherRequestPointcut() {

    }

    @Pointcut("weatherServicePointcut() && execution(* updateWeather(..))")
    protected void updateWeatherRequestPointcut() {

    }

    /**
     * Пытаемся достать из кэша погоду по городу и дате. Если в кэше есть значение, отдаем его,
     * иначе - продолжаем поход в сервис
     * @param pjp JoinPoint
     * @return объекты погоды
     * @throws Throwable pjp.proceed() может выбрасывать исключение
     */
    @Around(value = "getWeatherRequestPointcut()")
    public Object tryGetFromCache(ProceedingJoinPoint pjp) throws Throwable {
        WeatherRequestCacheKey key = getKeyFromGetRequestJoinPoint(pjp);
        List<WeatherDto> val = cache.get(key);
        if (val != null) {
            return val;
        }

        Object value = pjp.proceed();
        if (value instanceof List<?> returnList) {
            if (returnList.stream().allMatch(el -> el instanceof WeatherDto)) {
                cache.put(key, (List<WeatherDto>) returnList);
            }
        }

        return value;
    }

    /**
     * Когда значение обновляется вручную, кэш инвалидируется, чтобы не отдавать неконсистентные данные
     * @param cityId идентификатор города
     * @param createWeatherDto моделька для создания объекта погоды
     */
    @AfterReturning(
            value = "updateWeatherRequestPointcut() && args(cityId, createWeatherDto, ..)",
            argNames = "cityId, createWeatherDto"
    )
    public void invalidateCacheOnUpdate(UUID cityId, CreateWeatherDto createWeatherDto) {
        WeatherRequestCacheKey key = new WeatherRequestCacheKey(cityId, createWeatherDto.dateTime().toLocalDate());
        cache.invalidate(key);
    }

    private WeatherRequestCacheKey getKeyFromGetRequestJoinPoint(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        UUID cityId = (UUID) args[0];
        LocalDate date = (LocalDate) args[1];
        return new WeatherRequestCacheKey(cityId, date);
    }
}
