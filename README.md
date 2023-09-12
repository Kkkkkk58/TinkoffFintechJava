# Тинькофф Финтех: Java-разработчик '23
Карим Хасан, телеграм: [@KKhasan](https://t.me/KKhasan)

### Опыт, навыки
В настоящее время учусь в Университете ИТМО на третьем курсе, специальность - "Информационные системы и технологии". 
Работаю в ИТМО ментором по дисциплине **Основы программирования на C++** - отвечаю студентам на вопросы, провожу код-ревью, принимаю лабораторные работы и экзамены. Кроме этого, я занят в проекте Научно-образовательного центра математики ИТМО по созданию платформы для обучения математическим дисциплинам - в нём я занимаю позицию фронтенд-разработчика, пишу код на `TypeScript` с использованием `React`.
Я прошёл такие курсы, как **Школа бэкенд-разработки** (трек `Java`) от *Яндекса*, **Winter Java School** от *КРОК*, *Kysect Academy* по разработке на платформе `.NET`.
Участвовал в студенческих соревнованиях и хакатонах, например, **TenderHack**, **Математическая регата** от *Тинькофф* и многих других.
Что касается навыков: пишу код на Java (Spring, Spring Boot), C# (ASP<span>.</span>NET Core, EF Core), C/C++, Python, JavaScript/TypeScript. Хорошо разбираюсь в таких инструментах и средствах как git, Docker. Работал на практике с базами данных PostgreSQL, MySQL, MSSQL, MongoDB, Neo4j, H2. Писал бэкенд-приложения с многослойной архитектурой, чистой архитектурой, микросервисной архитектурой, применял механизмы авторизации, брокеры сообщений (Kafka, RabbitMQ), использовал профилировщики и средства визуализации (Prometheus, Grafana)

### Цели, что хочу получить от курса
Курс заинтересовал своей практической значимостью и дальнейшими перспективами трудоустройства в одной из крупнейших компаний на рынке. Хочется улучшить свои навыки разработки под "присмотром" опытных специалистов, чтобы понимать, к чему стремиться, и стать таким же профессионалом впоследствии

### Курсовой проект
С темой пока не определился :confused:

### Про git
##### `init`
\- команда, создающая репозиторий в текущей директории. Если включить отображение скрытых файлов, то можно увидеть появление папки `.git` :\)
```sh
$ cd tinkoff-hw
$ git init
```
[Подробнее](https://git-scm.com/docs/git-init)

##### `clone`
\- команда, позволяющая создать копию существующего (удалённо или локально) репозитория. В самом базовом сценарии достаточно указать путь до репозитория, можно также указать, в какую папку его склонировать
```sh
$ git clone https://github.com/Kkkkkk58/TinkoffFintechJava.git
```
[Подробнее](https://git-scm.com/docs/git-clone)

##### `add`
\- команда, позволяющая добавить файл в отслеживаемые системой контроля версий. Можно просто перечислить файлы, можно задать регулярное выражение (но с `git add *`, например, лучше быть осторожнее). Игнорируемые файлы не попадут в отслеживаемое, если не применить флаг `-f`, `--force`
```sh
$ git add README.md
```
[Подробнее](https://git-scm.com/docs/git-add)


##### `commit`
\- команда, создающая коммит с добавленными изменениями. Если не хочется гуглить, как выходить из вима, можно указать сообщение, добавив флаг `-m`
```sh
$ git add README.md
$ git commit -m "docs: add README"
```
[Подробнее](https://git-scm.com/docs/git-commit)

##### `push`
\- команда, позволяющая отправить коммиты из локального репозитория на удалённый репозиторий. Можно также явно указать алиас удалённого репозитория и ветку (напр., `origin master`), в которую нужно добавить изменения (если ветки не существует на ремоуте, она создастся). С помощью флага `--tags` можно также отправить теги, а флаг `--force` как и в остальных командах позволяет сделать то, за что можно потом бить по рукам
```sh
$ git add README.md
$ git commit -m "docs: add README"
$ git push
```
[Подробнее](https://git-scm.com/docs/git-push)

##### `pull`
\- команда, позволяющая вытянуть изменения с удалённого репозитория и добавить их в локальный репозиторий. По умолчанию это комбинация команд `fetch` и `merge`, но можно переопределить поведение, чтобы после `fetch` случался `rebase`. Всё так же можно указать алиас репозитория и ветку. В этом месте иногда можно встретиться с merge конфликтами
```sh
$ git pull
```

[Подробнее](https://git-scm.com/docs/git-pull)