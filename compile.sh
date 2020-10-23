echo "Compiling code..."
javac -d ./bin -cp ./lib/json-20140107.jar:./src @sources.txt

echo "Compiling tests..."
javac -d ./bin -cp avac -d ./bin -cp ./lib/org.junit4-4.3.1.jar:./lib/json-20140107.jar:./src @tests.txt
