# OOP_Game
The project to learn OO design concepts and MDI application development in Java  
  
## 1 Задача (Сдана)  
1. Научиться собирать проект с использованием Apache Maven.  
2. Провести рефакторинг и исправить баг (см. комментарии в коде).  
3. Исправить баг убегания робота за границу окна  
## 2 Задача (Техдолг - файлик с ресурсбандлами находится по абсолютному пути, а не берется из джарника; пересоздается окно gamewindow, вместо пересозоздания title, дублирование кода в классах с окнами лога и игры, вынести в абстрактный класс)  Тесты: убедиться что при инициализации язык = дефолтному. и то что происходит смена языка
1. Добавить окно с подтверждением на закрытие любого из окон.  
2. Добавить пункт меню "Выйти" (с обработкой аналогичной кнопке закрытия приложения).  
3. Добавить локализацию приложения (пункты меню, кнопки и т.д.).  
4. По умолчанию выбирается язык системы, но он может быть изменён через меню.  
