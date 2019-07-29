/*
 * @author sunys
 * @date 2019年3月4日
 */
package com.sunys.core.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunys.facade.annotation.ServiceParam;
import com.sunys.facade.exception.CommonEIEnum;
import com.sunys.facade.exception.BusiException;

/**
 * ServiceInvoke
 * @author sunys
 * @date 2019年3月4日
 */
public class ServiceInvoke {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ObjectMapper objectMapper;

	public Object doService(String serviceName,String methodName,Map<String,String> paramMap,boolean isParam) throws Exception {
		//根据参数从spring容器中获取要执行的service的代理对象
		Object serviceProxy = applicationContext.getBean(serviceName);
		//获取接口上的方法
		Method method = Arrays.stream(serviceProxy.getClass().getInterfaces()).flatMap(inter->Arrays.stream(inter.getMethods()))
			.filter((m)->m.getName().equals(methodName)).findFirst().orElseThrow(()->new BusiException(CommonEIEnum.NOSUCH_METHOD_EXCEPTION));
		//获取方法中的所有参数
		Parameter[] parameters = method.getParameters();
		//遍历方法中需要的参数，从请求参数中获取这些参数并解析成对应的数据类型
		Object[] args = Arrays.stream(parameters).map((parameter)->{
			//获取方法上参数的注解
			Optional<ServiceParam> serviceParamOpt = Optional.of(parameter.getAnnotation(ServiceParam.class));
			//获取注解上的value
			String paramName = serviceParamOpt.map(ServiceParam::value).get();
			//获取参数类型
			Type paramType = parameter.getParameterizedType();
			//获取参数是否为必须
			boolean require = serviceParamOpt.map(ServiceParam::require).get();
			Object obj = null;
			if(isParam){
				Optional<String> paramValueOpt = Optional.ofNullable(paramMap.get(paramName));
				if(require){
					//如果参数为必须，获取不到抛出异常
					String paramValue = paramValueOpt.orElseThrow(()->new BusiException(CommonEIEnum.REQUIRE_PARAM_NULL_EXCEPTION));
					obj = getParamValue(paramName, paramType, paramValue);
				}else{
					//如果参数不是必须，判断是否为空
					if(paramValueOpt.isPresent()){
						String paramValue = paramValueOpt.get();
						obj = getParamValue(paramName, paramType, paramValue);
					}
				}
			}else{
				//获取请求参数的json
				String jsonStr = paramMap.get("json");
				try {
					JsonNode rootNode = objectMapper.readTree(jsonStr);
					Optional<JsonNode> jsonNodeOpt = Optional.ofNullable(rootNode).map((node)->node.get(paramName));
					if(require){
						//如果参数为必须，获取不到抛出异常
						JsonNode jsonNode = jsonNodeOpt.orElseThrow(()->new BusiException(CommonEIEnum.REQUIRE_PARAM_NULL_EXCEPTION));
						obj = getJsonValue(paramName, paramType, jsonNode);
					}else{
						//如果参数不是必须，判断是否为空
						if(jsonNodeOpt.isPresent()){
							JsonNode jsonNode = jsonNodeOpt.get();
							obj = getJsonValue(paramName, paramType, jsonNode);
						}
					}
				} catch (IOException e) {
					Log.error(e);
					throw new BusiException(CommonEIEnum.JSON_PARSE_EXCEPTION);
				}
			}
			return obj;
		}).toArray();
		//获取代理对象的方法
		Method methodProxy = Arrays.stream(serviceProxy.getClass().getMethods()).filter((m)->m.getName().equals(methodName)).findFirst().orElseThrow(()->new BusiException(CommonEIEnum.NOSUCH_METHOD_EXCEPTION));
		//执行代理对象的方法
		Object result = methodProxy.invoke(serviceProxy, args);
		return result;
	}

	private Object getParamValue(String paramName,Type paramType,String paramValue){
		Object obj = null;
		if("_p".equals(paramName)){
			//分页参数
			obj = PageUtils.getPageParam(paramValue);
		}else{
			if(paramType instanceof Class){
				Class clazz = (Class) paramType;
				//请求参数的字符串转换成方法参数需要基本数据类型
				obj = VariantHelper.parseValue(paramValue, clazz);
			}else{
				throw new BusiException(CommonEIEnum.PARAMETER_CONVERT_EXCEPTION);
			}
		}
		return obj;
	}

	private Object getJsonValue(String paramName,Type paramType,JsonNode jsonNode) throws IOException {
		Object obj = null;
		if("_p".equals(paramName)){
			//分页参数
			obj = PageUtils.getPageParam(jsonNode.asText());
		}else{
			//json转成方法参数需要对象
			JavaType javaType = objectMapper.getTypeFactory().constructType(paramType);
			obj = objectMapper.readValue(jsonNode.traverse(), javaType);
		}
		return obj;
	}
}
