@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  UniversalFileFinder startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and UNIVERSAL_FILE_FINDER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=--module-path c:\data\apps\javafx-sdk-17.0.2\lib   --add-modules javafx.controls,javafx.fxml --add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED  --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED  --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=c:\data\apps\jdk-15
set JAVA_EXE=%JAVA_HOME%/bin/javaw.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\UniversalFileFinder.jar;%APP_HOME%\lib\javafx-fxml-17.0.2-win.jar;%APP_HOME%\lib\javafx-controls-17.0.2-win.jar;%APP_HOME%\lib\javafx-controls-17.0.2.jar;%APP_HOME%\lib\javafx-graphics-17.0.2-win.jar;%APP_HOME%\lib\javafx-graphics-17.0.2.jar;%APP_HOME%\lib\javafx-base-17.0.2-win.jar;%APP_HOME%\lib\javafx-base-17.0.2.jar;%APP_HOME%\lib\log4j-slf4j18-impl-2.17.1.jar;%APP_HOME%\lib\slf4j-api-2.0.0-alpha6.jar;%APP_HOME%\lib\ignite-guice-1.2.2.jar;%APP_HOME%\lib\jackson-annotations-2.13.1.jar;%APP_HOME%\lib\jackson-databind-2.13.1.jar;%APP_HOME%\lib\jackson-core-2.13.1.jar;%APP_HOME%\lib\log4j-core-2.17.1.jar;%APP_HOME%\lib\controlsfx-11.1.1.jar;%APP_HOME%\lib\guice-4.2.3.jar;%APP_HOME%\lib\ignite-common-1.2.2.jar;%APP_HOME%\lib\log4j-api-2.17.1.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\guava-27.1-jre.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-2.5.2.jar;%APP_HOME%\lib\error_prone_annotations-2.2.0.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.17.jar


@rem Execute UniversalFileFinder
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %UNIVERSAL_FILE_FINDER_OPTS%  -classpath "%CLASSPATH%" com.pimpelkram.universalfilefinder.Main %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable UNIVERSAL_FILE_FINDER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%UNIVERSAL_FILE_FINDER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
