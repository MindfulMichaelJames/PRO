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

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\httpcore-nio-4.4.4.jar;%APP_HOME%\lib\semargl-sesame-0.6.1.jar;%APP_HOME%\lib\semargl-rdfa-0.6.1.jar;%APP_HOME%\lib\sesame-rio-jsonld-4.0.2.jar;%APP_HOME%\lib\slf4j-simple-1.7.25.jar;%APP_HOME%\lib\sesame-rio-nquads-4.0.2.jar;%APP_HOME%\lib\httpcore-4.4.4.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.13.jar;%APP_HOME%\lib\httpclient-4.5.1.jar;%APP_HOME%\lib\jackson-annotations-2.6.3.jar;%APP_HOME%\lib\jackson-databind-2.6.3.jar;%APP_HOME%\lib\caffeine-2.1.0.jar;%APP_HOME%\lib\guava-19.0.jar;%APP_HOME%\lib\semargl-rdf-0.6.1.jar;%APP_HOME%\lib\automaton-1.11-8.jar;%APP_HOME%\lib\httpclient-cache-4.5.1.jar;%APP_HOME%\lib\xz-1.5.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\owlapi-apibinding-5.0.0.jar;%APP_HOME%\lib\jackson-core-2.6.3.jar;%APP_HOME%\lib\commons-rdf-api-0.1.0-incubating.jar;%APP_HOME%\lib\owlapi-compatibility-5.0.0.jar;%APP_HOME%\lib\sesame-rio-binary-4.0.2.jar;%APP_HOME%\lib\sesame-rio-rdfxml-4.0.2.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\jsonld-java-0.8.0.jar;%APP_HOME%\lib\axiom-impl-1.2.14.jar;%APP_HOME%\lib\httpcore-osgi-4.4.4.jar;%APP_HOME%\lib\fluent-hc-4.5.1.jar;%APP_HOME%\lib\org.semanticweb.hermit-1.3.8.500.jar;%APP_HOME%\lib\httpclient-osgi-4.5.1.jar;%APP_HOME%\lib\jsr305-2.0.1.jar;%APP_HOME%\lib\sesame-rio-turtle-4.0.2.jar;%APP_HOME%\lib\jaxen-1.1.4.jar;%APP_HOME%\lib\owlapi-api-5.0.0.jar;%APP_HOME%\lib\owlapi-parsers-5.0.0.jar;%APP_HOME%\lib\sesame-rio-datatypes-4.0.2.jar;%APP_HOME%\lib\apache-mime4j-core-0.7.2.jar;%APP_HOME%\lib\sesame-model-4.0.2.jar;%APP_HOME%\lib\sesame-util-4.0.2.jar;%APP_HOME%\lib\stax2-api-3.1.1.jar;%APP_HOME%\lib\geronimo-stax-api_1.0_spec-1.0.1.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\owlapi-tools-5.0.0.jar;%APP_HOME%\lib\guice-assistedinject-4.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\guice-4.0.jar;%APP_HOME%\lib\commons-logging-1.1.3.jar;%APP_HOME%\lib\sesame-rio-rdfjson-4.0.2.jar;%APP_HOME%\lib\sesame-rio-ntriples-4.0.2.jar;%APP_HOME%\lib\geronimo-activation_1.1_spec-1.1.jar;%APP_HOME%\lib\geronimo-javamail_1.4_spec-1.7.1.jar;%APP_HOME%\lib\axiom-api-1.2.14.jar;%APP_HOME%\lib\semargl-core-0.6.1.jar;%APP_HOME%\lib\guice-multibindings-4.0.jar;%APP_HOME%\lib\owlapi-impl-5.0.0.jar;%APP_HOME%\lib\httpmime-4.5.1.jar;%APP_HOME%\lib\sesame-rio-languages-4.0.2.jar;%APP_HOME%\lib\owlapi-distribution-5.0.0.jar;%APP_HOME%\lib\sesame-rio-n3-4.0.2.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\owlapi-rio-5.0.0.jar;%APP_HOME%\lib\sesame-rio-api-4.0.2.jar;%APP_HOME%\lib\commons-io-2.4.jar;%APP_HOME%\lib\sesame-rio-trix-4.0.2.jar;%APP_HOME%\lib\axiom-dom-1.2.14.jar;%APP_HOME%\lib\woodstox-core-asl-4.1.4.jar;%APP_HOME%\lib\sesame-rio-trig-4.0.2.jar;%APP_HOME%\lib\axiom-c14n-1.2.14.jar;%APP_HOME%\lib\owlapi-oboformat-5.0.0.jar;%APP_HOME%\lib\owlapi-fixers-5.0.0.jar;%APP_HOME%\lib\pref-reasoner-0.1.0.jar

@rem Execute ProjBuild
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PROJ_BUILD_OPTS%  -classpath "%CLASSPATH%" pref.New %CMD_LINE_ARGS%

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
