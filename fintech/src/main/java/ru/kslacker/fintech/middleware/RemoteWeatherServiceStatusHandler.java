package ru.kslacker.fintech.middleware;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import ru.kslacker.fintech.dto.RemoteErrorBodyDto;
import ru.kslacker.fintech.dto.RemoteErrorDto;
import ru.kslacker.fintech.exceptions.*;

public class RemoteWeatherServiceStatusHandler {

    public Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.is5xxServerError()) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new UnknownRemoteWeatherClientResponseException()));
        }
        if (isHandledErrorStatus(status)) {
            return response.bodyToMono(RemoteErrorDto.class)
                    .flatMap(body -> Mono.error(handleRemoteError(body.error())));
        }
        return Mono.just(response);
    }

    private boolean isHandledErrorStatus(HttpStatusCode status) {
        return status == HttpStatus.BAD_REQUEST || status == HttpStatus.UNAUTHORIZED || status == HttpStatus.FORBIDDEN;
    }

    private Throwable handleRemoteError(RemoteErrorBodyDto body) {
        return switch (body.code()) {
            case 1003 -> BadRemoteRequestException.LocationNotProvided();
            case 1005 -> BadRemoteRequestException.InvalidRequestUrl();
            case 1006 -> new RemoteServiceLocationNotFoundException();
            case 9000 -> BadRemoteRequestException.InvalidBulkRequestBody();
            case 9001 -> new RemoteRequestBulkBodyTooBigException();
            case 9999 -> new RemoteServiceUnavailableException();
            case 1002, 2006, 2007, 2008, 2009 -> new InvalidRemoteServiceApiKeyException();
            default -> new UnknownRemoteWeatherClientResponseException();
        };
    }
}
