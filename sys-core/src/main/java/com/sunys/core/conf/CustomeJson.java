package com.sunys.core.conf;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sunys.core.util.VariantHelper;
import com.sunys.facade.exception.ExceptionInfo;
import com.sunys.facade.exception.ExceptionInfoImpl;

/**
 * 序列化和反序列化json
 * CustomeJson
 * @author sunys
 * @date 2019年1月5日
 */
@JsonComponent
public class CustomeJson {

	public static class DateSerializer extends JsonSerializer<Date> {

		@Override
		public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeNumber(Optional.ofNullable(value).map(Date::getTime).orElse(null));
		}
	}

	public static class DateDeserializer extends JsonDeserializer<Date>{

		@Override
		public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			Date date = VariantHelper.parseDate(p.getText());
			return date;
		}
	}

	public static class ExceptionInfoSerializer extends JsonSerializer<ExceptionInfo> {

		@Override
		public void serialize(ExceptionInfo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeStartObject();
			gen.writeFieldName("level");
			gen.writeString(value.getLevel());
			gen.writeFieldName("code");
			gen.writeString(value.getCode());
			gen.writeFieldName("msg");
			gen.writeString(value.getMsg());
			gen.writeFieldName("isToast");
			gen.writeBoolean(value.getIsToast());
			gen.writeEndObject();
		}
	}

	public static class ExceptionInfoDeserializer extends JsonDeserializer<ExceptionInfo>{

		@Override
		public ExceptionInfo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			ExceptionInfoImpl info = p.readValueAs(ExceptionInfoImpl.class);
			return info;
		}
	}

}
