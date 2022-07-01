package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public abstract class JsonReader extends Reports{
    public JSONObject getDataFromJson() throws IOException, ParseException {
        JSONParser jsonParser=new JSONParser();
        FileReader fileReader=new FileReader("/Users/manojs/Documents/Automation/Playwright/PlayWrightJava/src/test/resources/Json/foodhub.json");
        Object obj=jsonParser.parse(fileReader);
        JSONObject jsonObject=(JSONObject) obj;
        return jsonObject;
    }

}
