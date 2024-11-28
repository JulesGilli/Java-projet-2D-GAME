@echo off
REM Répertoires
set PROJECT_ROOT=C:\Repositary\T-JAV-501-TLS_10
set TESTS_DIR=%PROJECT_ROOT%\core\src\test\java
set GAME_LAUNCHER_DIR=%PROJECT_ROOT%\lwjgl3\src\main\java\io\github\maingame\lwjgl3

REM Classes principales
set TEST_RUNNER_CLASS=Main
set GAME_MAIN_CLASS=io.github.maingame.lwjgl3.Lwjgl3Launcher

REM Compiler le projet avec Gradle
echo Compilation du projet...
cd %PROJECT_ROOT%
gradlew build
if %ERRORLEVEL% neq 0 (
    echo Erreur : Echec de la compilation.
    exit /b 1
)

REM Exécuter les tests
echo Exécution des tests...
java -cp "%PROJECT_ROOT%\core\build\classes\java\test;%PROJECT_ROOT%\core\build\libs\core.jar" %TEST_RUNNER_CLASS%
if %ERRORLEVEL% neq 0 (
    echo Erreur : Certains tests ont échoué.
    exit /b 1
)

REM Lancer le jeu si les tests réussissent
echo Tous les tests ont réussi. Lancement du jeu...
java -cp "%PROJECT_ROOT%\lwjgl3\build\classes\java\main;%PROJECT_ROOT%\lwjgl3\build\libs\lwjgl3.jar" %GAME_MAIN_CLASS%
if %ERRORLEVEL% neq 0 (
    echo Erreur : Impossible de lancer le jeu.
    exit /b 1
)

echo Fin de l'exécution.
