echo "Running unittests..."
java -javaagent:./lib/jacocoagent.jar -cp ./lib/org.junit4-4.3.1.jar:./lib/json-20140107.jar:./bin org.junit.runner.JUnitCore \
    test.Boggle.BoggleMode.BoggleModeTest \
echo "Generating report..."
java -jar ./lib/jacococli.jar report ./jacoco.exec --classfiles ./bin --html ./coveragereport --name CodeCoverageReport --sourcefiles ./src