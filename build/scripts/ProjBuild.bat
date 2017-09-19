@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  ProjBuild startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PROJ_BUILD_OPTS to pass JVM options to this script.
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
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\pref-reasoner-0.1.0.jar;%APP_HOME%\lib\owlapi-distribution-5.1.1.jar;%APP_HOME%\lib\org.semanticweb.hermit-1.3.8.4.jar;%APP_HOME%\lib\owlapi-compatibility-5.1.1.jar;%APP_HOME%\lib\jackson-core-2.8.5.jar;%APP_HOME%\lib\jackson-databind-2.8.5.jar;%APP_HOME%\lib\jackson-annotations-2.8.5.jar;%APP_HOME%\lib\commons-rdf-api-0.3.0-incubating.jar;%APP_HOME%\lib\xz-1.6.jar;%APP_HOME%\lib\slf4j-api-1.7.22.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.22.jar;%APP_HOME%\lib\rdf4j-model-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-api-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-languages-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-datatypes-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-binary-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-n3-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-nquads-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-ntriples-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-rdfjson-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-jsonld-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-rdfxml-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-trix-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-turtle-2.1.4.jar;%APP_HOME%\lib\rdf4j-rio-trig-2.1.4.jar;%APP_HOME%\lib\rdf4j-util-2.1.4.jar;%APP_HOME%\lib\jsonld-java-0.9.0.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\caffeine-2.3.5.jar;%APP_HOME%\lib\guava-20.0.jar;%APP_HOME%\lib\guice-4.1.0.jar;%APP_HOME%\lib\guice-assistedinject-4.1.0.jar;%APP_HOME%\lib\guice-multibindings-4.1.0.jar;%APP_HOME%\lib\jsr305-3.0.1.jar;%APP_HOME%\lib\commons-io-2.5.jar;%APP_HOME%\lib\commons-logging-1.1.3.jar;%APP_HOME%\lib\axiom-api-1.2.14.jar;%APP_HOME%\lib\axiom-c14n-1.2.14.jar;%APP_HOME%\lib\axiom-impl-1.2.14.jar;%APP_HOME%\lib\axiom-dom-1.2.14.jar;%APP_HOME%\lib\automaton-1.11-8.jar;%APP_HOME%\lib\geronimo-activation_1.1_spec-1.1.jar;%APP_HOME%\lib\geronimo-javamail_1.4_spec-1.7.1.jar;%APP_HOME%\lib\jaxen-1.1.4.jar;%APP_HOME%\lib\geronimo-stax-api_1.0_spec-1.0.1.jar;%APP_HOME%\lib\apache-mime4j-core-0.7.2.jar;%APP_HOME%\lib\woodstox-core-asl-4.1.4.jar;%APP_HOME%\lib\stax2-api-3.1.1.jar;%APP_HOME%\lib\owlapi-apibinding-5.1.1.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\owlapi-api-5.1.1.jar;%APP_HOME%\lib\owlapi-impl-5.1.1.jar;%APP_HOME%\lib\owlapi-parsers-5.1.1.jar;%APP_HOME%\lib\owlapi-oboformat-5.1.1.jar;%APP_HOME%\lib\owlapi-tools-5.1.1.jar;%APP_HOME%\lib\owlapi-rio-5.1.1.jar;%APP_HOME%\lib\httpclient-osgi-4.5.2.jar;%APP_HOME%\lib\httpcore-osgi-4.4.4.jar;%APP_HOME%\lib\httpclient-4.5.2.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\httpmime-4.5.2.jar;%APP_HOME%\lib\httpclient-cache-4.5.2.jar;%APP_HOME%\lib\fluent-hc-4.5.2.jar;%APP_HOME%\lib\httpcore-4.4.4.jar;%APP_HOME%\lib\httpcore-nio-4.4.4.jar

@rem Execute ProjBuild
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PROJ_BUILD_OPTS%  -classpath "%CLASSPATH%" pref.Demo %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PROJ_BUILD_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PROJ_BUILD_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
