/*
 * File:    EntityParser.java
 * Package: main.entity.base.parser
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base.parser;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class EntityParser implements TypeAdapterFactory {
    
    //Methods
    
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        
        return new TypeAdapter<>() {
            
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }
            
            @Override
            public T read(JsonReader in) throws IOException {
                final T entity = delegate.read(in);
                if (entity instanceof SerializableEntity) {
                    ((SerializableEntity) entity).initialize();
                }
                return entity;
            }
            
        };
    }
    
}
