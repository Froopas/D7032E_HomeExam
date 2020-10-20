echo "Compiling code..."
javac -d ./bin -cp \
    ./src \
    ./src/Player/Player.java \
    ./src/Player/PlayerUI/PlayerUI.java \
    ./src/Player/PlayerUI/AsciiPlayerUI.java \
    ./src/Language/LanguageInfo.java \
    ./src/Language/LanguageHandler.java \
    ./src/Boggle/Board.java \
    ./src/Boggle/BoggleMode/BoggleMode.java \
    ./src/Boggle/BoggleMode/StandardBoggle.java \

echo "Compiling tests..."
javac -d ./bin -cp ./lib/org.junit4-4.3.1.jar:./src \
    ./src/test/Player/PlayerUI/PlayerUITest.java \
    ./src/test/Language/LanguageHandlerTest.java \
    ./src/test/Boggle/BoggleMode/StandardBoggleTest.java
