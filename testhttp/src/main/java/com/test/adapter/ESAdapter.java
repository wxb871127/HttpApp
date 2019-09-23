package com.test.adapter;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.test.NonstandardBaseResult;
import com.test.ProjectUrlResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ESAdapter extends TypeAdapter {
    @Override
    public void write(JsonWriter out, Object value) throws IOException {

    }

    @Override
    public Object read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginObject();
        while(in.hasNext()){
            String tagName = in.nextName();
            Log.e("xxxxxxxxxxx", tagName);
            if("hits".equals(tagName)){
                return readHit(in);
            }else in.skipValue();
        }
        in.endObject();
        return null;
    }

    private List<NonstandardBaseResult<ProjectUrlResult>> readHit(JsonReader reader) throws IOException {
        List<NonstandardBaseResult<ProjectUrlResult>> bean = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String tagName = reader.nextName();
            if ("hits".equals(tagName) && reader.peek() != JsonToken.NULL) {
                reader.beginArray();
                NonstandardBaseResult<ProjectUrlResult> nonstandardBaseResult = new NonstandardBaseResult();
                while (reader.hasNext()){
                    ProjectUrlResult projectUrlResult = new ProjectUrlResult();
                    tagName = reader.nextName();
                    if("_index".equals(tagName)){
                        nonstandardBaseResult.set_index(reader.nextString());
                    }else if("_type".equals(tagName)){
                        nonstandardBaseResult.set_type(reader.nextString());
                    }else if("_id".equals(tagName)){
                        nonstandardBaseResult.set_id(reader.nextString());
                    }else if("_score".equals(tagName)){
                        nonstandardBaseResult.set_score(reader.nextInt());
                    }else if("_source".equals(tagName)){
                        reader.beginObject();
                        while (reader.hasNext()){
                            tagName = reader.nextName();
                            if("id".equals(tagName)){
                                projectUrlResult.setId(reader.nextString());
                            }else if("tjzxmc".equals(tagName)){
                                projectUrlResult.setTjzxmc(reader.nextString());
                            }else if("tjzxdz".equals(tagName)){
                                projectUrlResult.setTjzxdz(reader.nextString());
                            }else if("tjzxsqm".equals(tagName)){
                                projectUrlResult.setTjzxsqm(reader.nextString());
                            }
                        }
                        reader.endObject();
                    }
                    nonstandardBaseResult.set_source(projectUrlResult);
                }
                reader.endArray();
                bean.add(nonstandardBaseResult);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return bean;
    }
}
