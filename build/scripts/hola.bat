@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  hola startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and HOLA_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%/app;%APP_HOME%\lib\hola.jar;%APP_HOME%\lib\ratpack-groovy-1.6.1.jar;%APP_HOME%\lib\ratpack-guice-1.6.1.jar;%APP_HOME%\lib\ratpack-core-1.6.1.jar;%APP_HOME%\lib\h2-1.4.197.jar;%APP_HOME%\lib\slf4j-simple-1.7.25.jar;%APP_HOME%\lib\ratpack-exec-1.6.1.jar;%APP_HOME%\lib\netty-codec-http-4.1.32.Final.jar;%APP_HOME%\lib\netty-handler-4.1.32.Final.jar;%APP_HOME%\lib\javax.activation-1.2.0.jar;%APP_HOME%\lib\caffeine-2.6.2.jar;%APP_HOME%\lib\javassist-3.22.0-GA.jar;%APP_HOME%\lib\jackson-datatype-guava-2.9.8.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.9.8.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.9.8.jar;%APP_HOME%\lib\jackson-databind-2.9.8.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.9.8.jar;%APP_HOME%\lib\snakeyaml-1.23.jar;%APP_HOME%\lib\groovy-all-2.4.15.jar;%APP_HOME%\lib\ratpack-base-1.6.1.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.32.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-codec-4.1.32.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.32.Final.jar;%APP_HOME%\lib\netty-transport-4.1.32.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.32.Final.jar;%APP_HOME%\lib\reactive-streams-1.0.2.jar;%APP_HOME%\lib\jackson-annotations-2.9.0.jar;%APP_HOME%\lib\jackson-core-2.9.8.jar;%APP_HOME%\lib\guice-4.1.0.jar;%APP_HOME%\lib\guice-multibindings-4.1.0.jar;%APP_HOME%\lib\guava-21.0.jar;%APP_HOME%\lib\netty-resolver-4.1.32.Final.jar;%APP_HOME%\lib\netty-common-4.1.32.Final.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\aopalliance-1.0.jar
cd "%APP_HOME%/app"

@rem Execute hola
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %HOLA_OPTS%  -classpath "%CLASSPATH%" ratpack.groovy.GroovyRatpackMain %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable HOLA_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%HOLA_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
