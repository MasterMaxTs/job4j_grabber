
Проект. Агрегатор Java вакансий        
[![Build Status](https://app.travis-ci.com/MasterMaxTs/project_grabber.svg?branch=master)](https://app.travis-ci.com/MasterMaxTs/project_grabber)
[![codecov](https://codecov.io/gh/MasterMaxTs/project_grabber/branch/master/graph/badge.svg?token=P6BBJSCD5K)](https://codecov.io/gh/MasterMaxTs/project_grabber)

Программа считывает с сайта sql.ru раздела job все вакансии, относящиеся к Java, и записывает их в базу данных.
Программа использует планировщик для запуска по расписанию. Период запуска указывается в настройках app.properties.
События исполнения программы записываются логгером и выводятся в консоль.
Результат выполнения программы выводится в консоль, так же предусмотрен вывод в виде html-страницы.

Расширение.

1. В проект можно добавить новые сайты без изменения кода.
2. В проекте можно сделать параллельный парсинг сайтов.

Используемые технологии:
Maven, Java Core, библиотека quartz-scheduler, Travis CI, JaCoCo.
Доступ к интерфейсу через REST API.
СУБД PostgreSQL