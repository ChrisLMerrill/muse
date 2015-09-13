The examples in this folder are used to produce the classes and JARs needed to test dynamic
classpath loading.

The gradle project here is independent from gradle projects in folders above. Run it to build 
and test the custom (Java) MuseSteps that will be needed to test dynamic classloading in the 
classpath project.


When this project is updated:

1) copy IncrementVariableByN.class
from \core\src\test\other_resources\projects\StuffNeeededForClasspath\build\classes\main\org\musetest\examples
to \core\src\test\other_resources\projects\classpath\classes\org\musetest\examples

2) copy \core\src\test\other_resources\projects\StuffNeeededForClasspath\build\libs\StuffNeededForClasspath.jar
to \builtins\src\test\other_resources\projects\classpath\lib\CustomJavaStepLib.jar
3) In the above jar, after copying, remove IncrementVariableByN.class, leaving only the MultiplyVariableByN.class