package com.ooadprojectserver.restaurantmanagement.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ooadprojectserver.restaurantmanagement.exception.AppException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.*;

public class DataUtil {
    private static final Logger log = LogManager.getLogger(DataUtil.class);
    public static final String PHONE_NUMBER_PATTERN = "^(([03+[2-9]|05+[6|8|9]|07+[0|6|7|8|9]|08+[1-9]|09+[1-4|6-9]]){3})+[0-9]{7}$";
    public static final String EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]+";
    public static final String IMAGE_EXTENSION_PATTERN = "^(gif|jpe?g|tiff?|png|webp|bmp)$";
    public static final String REGEX_BASE64 = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";
    public static final String REGEX_FILE_NAME = "^.+\\.[A-Za-z]+$";
    public static final String REGEX_CRON_EXPRESSION = "^((\\*|\\?|\\d+((\\/|\\-){0,1}(\\d+))*)\\s*){6}$";

    private static final Random random = new Random();

    public enum DataTypeClassName {
        UUID(UUID.class.getName()),
        STRING(String.class.getName()),
        LONG(Long.class.getName()),
        PRIMITIVE_LONG(long.class.getName()),
        DOUBLE(Double.class.getName()),
        PRIMITIVE_DOUBLE(double.class.getName()),
        BOOLEAN(Boolean.class.getName()),
        PRIMITIVE(boolean.class.getName()),
        DATE(Date.class.getName()),
        BIG_DECIMAL(BigDecimal.class.getName()),
        INTEGER(Integer.class.getName()),
        INT(int.class.getName());

        public final String value;
        private static final Map<String, DataTypeClassName> lookup = new HashMap<>();

        static {
            for (DataTypeClassName d : DataTypeClassName.values()) {
                lookup.put(d.value, d);
            }
        }

        public static DataTypeClassName get(String value) {
            return lookup.get(value);
        }

        DataTypeClassName(String value) {
            this.value = value;
        }
    }

    public static final class CONVERT_QUERY_RESULT_OPTIONS {
        public static final String DIGITS_ROUND = "digitsRound";
    }

    public static class PARAM_TYPE {
        public static final int PARAM_GENERAL = 0;
        public static final int PARAM_OPTION = 1;
    }

    public enum FileSizeUnit {B, KB, MB, GB}

    /*
     * @description: Convert object to url encoded
     * */
    public static String toUrlEncoded(Object obj) {
        Map<String, Object> map = Singleton.getObjectMapper().convertValue(obj, Map.class);
        Map<String, String> newMap = new HashMap<>();
        map.forEach((key, value) -> newMap.put(key, safeToString(value)));

        return toUrlEncoded(newMap);
    }

    /*
     * @description: Convert map to url encoded
     * */
    public static String toUrlEncoded(Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        map.forEach((key, value) -> {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(safeToString(value), StandardCharsets.UTF_8));
        });
        return result.toString();
    }

    /*
     * @description: Convert string to url encoded
     * */
    public static String toUrlEncoded(String value) {
        return URLEncoder.encode(safeToString(value), StandardCharsets.UTF_8);
    }

    /*
     * @description: Clone Bean
     * */
    public static <T> T cloneBean(T source) {
        try {
            if (source == null) {
                return null;
            }
            T dto = (T) source.getClass().getConstructor().newInstance();
            BeanUtils.copyProperties(source, dto);
            return dto;
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    /*
     * @description: Set parameter to pattern
     * */
    public static String setParameters(String format, Object data, int type) throws Exception {
        final String WRAP_PARAM_PATTERN = "[><]";
        final String OPTION_PARAM_PATTERN = "[\\[\\]]";

        String result = format;

        if (!format.contains("[")) {
            //not included option
            String[] params = format.split("<|>_<|>");
            Field[] fields = data.getClass().getDeclaredFields();

            for (String param : params) {
                if (isNullOrEmpty(param)) continue;
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (param.equals(fieldName)) {
                        String value = safeToString(field.get(data));

                        if (type == PARAM_TYPE.PARAM_GENERAL) {
                            result = result.replace(param, value);
                        } else if (type == PARAM_TYPE.PARAM_OPTION) {
                            if (!isNullOrEmpty(value)) {
                                return value;
                            }
                        }

                        break;
                    }
                }
            }

            if (type == PARAM_TYPE.PARAM_OPTION) return "";

            result = result.replaceAll(WRAP_PARAM_PATTERN, "");
        } else {
            //include option
            List<String> lstOptions = Arrays.asList(format.split(OPTION_PARAM_PATTERN));
            StringBuilder temp = new StringBuilder();

            for (int i = 0; i < lstOptions.size(); i++) {
                if (i % 2 != 0) {
                    String paramOption = lstOptions.get(i);

                    String rsOption = setParameters(paramOption, data, PARAM_TYPE.PARAM_OPTION);
                    lstOptions.set(i, rsOption);
                }
                temp.append(lstOptions.get(i));
            }

            result = setParameters(temp.toString(), data, PARAM_TYPE.PARAM_GENERAL);
        }

        return result;
    }

    /*
     * @description: Pretty object
     * */
    public static String prettyObject(Object object) {
        try {
            return Singleton.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /*
     * @description: Convert Object to Json
     * */
    public static String objectToJson(Object object) {
        if (object == null) return "";

        try {
            return Singleton.getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    /*
     * @description: Convert object to class
     * */
    public static <T> T objectToClass(Object object, Class<T> targetClass) {
        return Singleton.getObjectMapper().convertValue(object, targetClass);
    }

    /*
     * @description: Convert object
     * */
    public static <T> List<T> objectToListClass(Object object, Class<T> targetClass) throws Exception {
        String jsonData = objectToJson(object);

        ObjectMapper objectMapper = Singleton.getObjectMapper();
        return objectMapper.readValue(
                jsonData,
                objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass)
        );
    }

    /*
     * @description: UUID to String with default value
     * */
    public static UUID safeToUUID(Object obj1) {
        var id = objectToClass(obj1, UUID.class);
        log.info("id: {}", id);
        return id;
    }



    /*
     * @description: Safe to string with default value
     * */
    public static String safeToString(Object obj1, String defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }

        return obj1.toString().trim();
    }

    /*
     * @description: Safe to string
     * */
    public static String safeToString(Object obj1) {
        return safeToString(obj1, "");
    }

    /*
     * @description: Safe to Long with default value
     * */
    public static Long safeToLong(Object obj1, Long defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        if (obj1 instanceof BigDecimal) {
            return ((BigDecimal) obj1).longValue();
        }
        if (obj1 instanceof BigInteger) {
            return ((BigInteger) obj1).longValue();
        }
        if (obj1 instanceof Double) {
            return ((Double) obj1).longValue();
        }

        try {
            return Long.parseLong(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    /*
     * @description: Safe to Long
     * */
    public static Long safeToLong(Object obj1) {
        return safeToLong(obj1, 0L);
    }

    /*
     * @description: Safe to Double with default value
     * */
    public static Double safeToDouble(Object obj1, Double defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    /*
     * @description: Safe to Double
     * */
    public static Double safeToDouble(Object obj1) {
        return safeToDouble(obj1, 0.0);
    }

    /*
     * @description: Safe to Short with default value
     * */
    public static Short safeToShort(Object obj1, Short defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    /*
     * @description: Safe to Short
     * */
    public static Short safeToShort(Object obj1) {
        return safeToShort(obj1, (short) 0);
    }

    /*
     * @description: Safe to Int with default value
     * */
    public static Integer safeToInt(Object obj1, Integer defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            String s = obj1.toString();
            if (s.contains(".")) s = s.substring(0, s.lastIndexOf("."));

            return Integer.parseInt(s);
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    /*
     * @description: Safe to Int
     * */
    public static Integer safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }

    /*
     * @description: Safe to BigDecimal
     * */
    public static BigDecimal safeToBigDecimal(Object obj1) {
        if (obj1 == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return BigDecimal.ZERO;
        }
    }

    /*
     * @description: Safe to BigInteger
     * */
    public static BigInteger safeToBigInteger(Object obj1) {
        return safeToBigInteger(obj1, BigInteger.ZERO);
    }

    /*
     * @description: Safe to BigInteger with default value
     * */
    public static BigInteger safeToBigInteger(Object obj1, BigInteger defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return new BigInteger(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    /*
     * @description: Safe Integer to Boolean
     * */
    public static Boolean safeToBoolean(Integer data) {
        return safeToBoolean(data, false);
    }

    /*
     * @description: Safe Integer to Boolean with default value
     * */
    public static Boolean safeToBoolean(Integer data, Boolean defaultValue) {
        if (isNullObject(data)) return defaultValue;
        return data.equals(1);
    }

    /*
     * @description: Safe String to Boolean
     * */
    public static Boolean safeToBoolean(String data) {
        return safeToBoolean(data, false);
    }

    /*
     * @description: Safe String to Boolean with default value
     * */
    public static Boolean safeToBoolean(String data, Boolean defaultValue) {
        if (isNullOrEmpty(data)) return defaultValue;
        return data.equals("true") || data.equals("1");
    }

    /*
     * @description: Safe Double to Boolean
     * */
    public static Boolean safeToBoolean(Double data) {
        return safeToBoolean(data, false);
    }

    /*
     * @description: Safe Double to Boolean with default value
     * */
    public static Boolean safeToBoolean(Double data, Boolean defaultValue) {
        if (isNullObject(data)) return defaultValue;
        return data == 1;
    }

    /*
     * @description: Safe Long to Boolean
     * */
    public static Boolean safeToBoolean(Long data) {
        return safeToBoolean(data, false);
    }

    /*
     * @description: Safe Long to Boolean with default value
     * */
    public static Boolean safeToBoolean(Long data, Boolean defaultValue) {
        if (isNullObject(data)) return defaultValue;
        return data == 1;
    }

    /*
     * @description: Safe Boolean to Boolean
     * */
    public static Boolean safeToBoolean(Boolean data) {
        return safeToBoolean(data, false);
    }

    /*
     * @description: Safe Boolean to Boolean with default value
     * */
    public static Boolean safeToBoolean(Boolean data, Boolean defaultValue) {
        if (isNullObject(data)) return defaultValue;
        return data;
    }

    /*
     * @description: Safe to list
     * */
    public static <T> List<T> safeToList(List<T> data, List<T> defaultValue) {
        if (isNullOrEmpty(data)) return defaultValue;
        return data;
    }

    public static <T> List<T> safeToList(List<T> data) {
        return safeToList(data, new ArrayList<>());
    }

    /*
     * @description: Safe Abs
     * */
    public static Long safeAbs(Long number) {
        return safeAbs(number, 0L);
    }

    /*
     * @description: Safe Abs with default value
     * */
    public static Long safeAbs(Long number, Long defaultValue) {
        if (number == null) {
            if (defaultValue == null) {
                return 0L;
            }
            return defaultValue < 0 ? -defaultValue : defaultValue;
        }

        return number < 0 ? -number : number;
    }

    /*
     * @description: Compare two Integer variable
     * */
    public static boolean safeEqual(Integer obj1, Integer obj2) {
        if (Objects.equals(obj1, obj2)) return true;
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    /*
     * @description: Compare two Double variable
     * */
    public static boolean safeEqual(Double obj1, Double obj2) {
        if (Objects.equals(obj1, obj2)) return true;
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    /*
     * @description: Compare two Long variable
     * */
    public static boolean safeEqual(Long obj1, Long obj2) {
        if (Objects.equals(obj1, obj2)) return true;
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    /*
     * @description: Compare two BigDecimal variable
     * */
    public static boolean safeEqual(BigInteger obj1, BigInteger obj2) {
        if (Objects.equals(obj1, obj2)) return true;
        return (obj1 != null) && obj1.equals(obj2);
    }

    /*
     * @description: Compare two Short variable
     * */
    public static boolean safeEqual(Short obj1, Short obj2) {
        if (Objects.equals(obj1, obj2)) return true;
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    /*
     * @description: Compare two String variable with case-sensitive
     * */
    public static boolean safeEqualCaseSensitive(String obj1, String obj2) {
        return Objects.equals(obj1, obj2);
    }

    /*
     * @description: Compare two String variable
     * */
    public static boolean safeEqual(String obj1, String obj2) {
        if (Objects.equals(obj1, obj2)) return true;
        return ((obj1 != null) && obj1.equalsIgnoreCase(obj2));
    }

    /*
     * @description: Compare two Object
     * */
    public static boolean safeEqual(Object obj1, Object obj2) {
        try {
            if (obj1 == null && obj2 == null) {
                return true;
            } else if (obj1 == null || obj2 == null) {
                return false;
            }

            if (obj1 instanceof Date) {
                return compare(safeToDate(obj1), safeToDate(obj2)) == 0;
            } else if (obj1 instanceof String) {
                return String.valueOf(obj1).equals(String.valueOf(obj2));
            } else if (obj1 instanceof Long) {
                return safeToLong(obj1).equals(safeToLong(obj2));
            } else if (obj1 instanceof Integer) {
                return safeToInt(obj1).equals(safeToInt(obj2));
            } else {
                return obj1 == obj2;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /*
     * @description: Safe equal boolean
     * */
    public static boolean safeEqual(Boolean obj1, Boolean obj2) {
        return Objects.equals(obj1, obj2);
    }

    /*
     * @description: Safe trim
     * */
    public static String safeTrim(String obj) {
        if (obj == null) return null;
        return obj.trim();
    }

    /*
     * @description: Safe to upper case
     * */
    public static String safeToUpperCase(String obj) {
        if (obj == null) return null;
        return obj.toUpperCase();
    }

    /*
     * @description: Safe to lower case
     * */
    public static String safeToLowerCase(String obj) {
        if (obj == null) return null;
        return obj.toLowerCase();
    }

    /*
     * @description: Check string is email
     * */
    public static Boolean isEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    /*
     * @description: Check string is base64
     * */
    public static boolean isBase64(String string) {
        return string.matches(REGEX_BASE64);
    }

    /*
     * @description: Check String is number
     * */
    public static boolean isNumber(String str) {
        return str.matches("(\\d+)");
    }

    /*
     * @description: Check character null or empty
     * */
    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /*
     * @description: Check collection null or empty
     * */
    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /*
     * @description: Check array null or empty
     * */
    public static boolean isNullOrEmpty(final Object[] collection) {
        return collection == null || collection.length == 0;
    }

    /*
     * @description: Check map null or empty
     * */
    public static boolean isNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /*
     * @description: Check Long is null or zero
     * */
    public static boolean isNullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    /*
     * @description: Check Double is null or zero
     * */
    public static boolean isNullOrZero(Double value) {
        return (value == null || value == 0);
    }

    /*
     * @description: Check String is null or zero
     * */
    public static boolean isNullOrZero(String value) {
        return (value == null || safeToLong(value).equals(0L));
    }

    /*
     * @description: Check Integer is null or zero
     * */
    public static boolean isNullOrZero(Integer value) {
        return (value == null || value.equals(0));
    }

    /*
     * @description: Check BigDecimal is null or zero
     * */
    public static boolean isNullOrZero(BigDecimal value) {
        return (value == null || value.compareTo(BigDecimal.ZERO) == 0);
    }

    /*
     * @description: Check Object is null
     * */
    public static boolean isNullObject(Object obj1) {
        if (obj1 == null) {
            return true;
        }
        if (obj1 instanceof String) {
            return isNullOrEmpty(obj1.toString());
        }
        return false;
    }

    /*
     * @description: Clear white space
     * */
    public static String clearSpace(String data) {
        if (isNullOrEmpty(data)) return data;
        return data.replaceAll("\\s+", "");
    }

    /*
     * @description: Remove unicode character
     * */
    public static String removeUnicode(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /*
     * @description: Convert to UserName
     * */
    public static String toUsername(String name) {
        String res = removeUnicode(name);
        String[] separatedName = res.split(" ");
        int len = separatedName.length - 1;
        StringBuilder username = new StringBuilder(separatedName[len]);
        for (int i = 0; i < len; i++) {
            username.append(separatedName[i].charAt(0));
        }
        return username.toString();
    }

    /*
     * @description: Convert List to Map
     * */
    public static Map<String, Object> toMap(List<Object> data) {
        Map<String, Object> result = new HashMap<>();

        if (!isNullOrEmpty(data)) {
            for (int i = 0; i < data.size(); i += 2) {
                String key = safeToString(data.get(i));
                Object value = data.get(i + 1);

                result.put(key, value);
            }
        }

        return result;
    }

    /*
     * @description: Generate OTP
     * */
    public static String generateOTP(int length) {
        String numbers = "0123456789";
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(numbers.charAt(ThreadLocalRandom.current().nextInt(numbers.length())));
        }
        return otp.toString();
    }


    /*
     * @description: Safe to slug
     * */
    public static String toSlug(String str, Boolean replaceSpecialChar) {
        // Remove unicode character
        str = removeUnicode(str);

        // Convert to lower case
        str = str.toLowerCase();

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c) || !replaceSpecialChar) {
                sb.append(c);
            }
        }

        // Replace space character to strikethrough
        return sb.toString().trim().replaceAll("\\s+", "-");
    }

    /*
     * @description: Convert to like conditional in sql
     * */
    public static String toLikeConditional(String conditional) {
        if (isNullOrEmpty(conditional)) return "%%";

        return "%" + conditional.replace("%", "\\%").replace("_", "\\_").toUpperCase() + "%";
    }

    /*
     * @description: Round
     * */
    public static Double round(Double number, Integer digits) {
        number = safeToDouble(number);
        digits = safeToInt(digits);

        if (digits < 0) {
            throw new IllegalArgumentException("Số chữ số sau dấu thập phân phải là số nguyên dương.");
        }

        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(digits, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    /*
     * @description: Encode base64
     * */
    public static String encodesBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static String decodeBase64(String dataBase64) {
        byte[] decodedBytes = Base64.getDecoder().decode(dataBase64);
        return new String(decodedBytes);
    }

    /*
     * @description: Get all item difference in two list
     * */
    public static <T> List<T> difference(Collection<T> lstOne, Collection<T> lstTwo) {
        if (isNullOrEmpty(lstOne))
            return new ArrayList<>(lstTwo);

        if (isNullOrEmpty(lstTwo))
            return new ArrayList<>(lstOne);

        List<T> differences = new ArrayList<>(lstOne);
        differences.removeAll(lstTwo);
        return differences;
    }


    /*
     * @description: Remove duplicate item
     * */
    public static List<Object> removeDuplicate(List<Object> data) {
        List<Object> result = new ArrayList<>();

        for (Object item : data) {
            if (item instanceof String) {
                item = safeToString(item).toLowerCase();
            }

            if (result.contains(item)) continue;
            result.add(item);
        }

        return result;
    }

    /*
     * @description: Remove duplicate item by field name
     * */
    public static <T> List<T> removeDuplicate(List<T> data, String fieldName) {
        try {
            Field field = data.get(0).getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            Set<Object> set = new HashSet<>();
            List<T> result = new ArrayList<>();

            for (T item : data) {
                Object value = field.get(item);
                if (value instanceof String) {
                    value = safeToString(value).toLowerCase();
                }

                if (set.add(value)) result.add(item);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    /*
     * @description: Remove duplicate item by list field name
     * */
    public static <T> List<T> removeDuplicate(List<T> data, List<String> listFieldName) {
        try {
            List<Field> listField = listFieldName.stream().map(fieldName -> {
                try {
                    Field field = data.get(0).getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);

                    return field;
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());

            Set<String> set = new HashSet<>();
            List<T> result = new ArrayList<>();

            for (T item : data) {
                List<String> listValue = listField.stream().map(field -> {
                    try {
                        return safeToString(field.get(item)).toLowerCase();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

                if (set.add(String.join(",", listValue))) result.add(item);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public static Boolean hasDuplicate(List<Object> data) {
        List<Object> listRemoveDuplicate = removeDuplicate(data);

        return listRemoveDuplicate.size() < data.size();
    }

    public static <T> Boolean hasDuplicate(List<T> data, String fieldName) {
        List<T> listRemoveDuplicate = removeDuplicate(data, fieldName);

        return listRemoveDuplicate.size() < data.size();
    }

    public static <T> Boolean hasDuplicate(List<T> data, List<String> listFieldName) {
        List<T> listRemoveDuplicate = removeDuplicate(data, listFieldName);

        return listRemoveDuplicate.size() < data.size();
    }

    /*
     * @description: Get largest list
     * */
    public static <T> List<T> getLargestList(List<List<T>> sourceData) {
        if (!sourceData.isEmpty()) {
            return sourceData.stream()
                    .max(Comparator.comparingInt(List::size))
                    .orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    /*
     * @description: Check two list has common item
     * */
    public static Boolean hasCommonItem(List<?> firstList, List<?> secondList) {
        if (isNullOrEmpty(firstList) || isNullOrEmpty(secondList)) return false;
        return firstList.stream().anyMatch(secondList::contains);
    }

    /*
     * @description: Get value in map by key
     * */
    public static String getMapValue(Map<?, ?> data, String key) {
        if (isNullOrEmpty(data)) return "";
        return safeToString(data.get(key));
    }

    /*
     * @description: Get base64 size
     * */
    public static double getBase64Size(String data) {
        return getBase64Size(data, FileSizeUnit.MB);
    }

    public static double getBase64Size(String data, FileSizeUnit unit) {
        int unitMultiplier = -1;
        switch (unit) {
            case B:
                unitMultiplier = 1;
                break;
            case KB:
                unitMultiplier = 1024;
                break;
            case MB:
                unitMultiplier = 1024 * 1024;
                break;
            case GB:
                unitMultiplier = 1024 * 1024 * 1024;
        }
        if (unitMultiplier == -1) {
            throw new IllegalArgumentException("Invalid unit");
        }

        byte[] bytes = Base64.getDecoder().decode(data);
        double size = (double) bytes.length / unitMultiplier;

        return Math.round(size);
    }

    /*
     * @description: Get first name
     * */
    public static String getFirstName(String fullName) {
        if (isNullOrEmpty(fullName)) return "";

        String[] nameParts = fullName.trim().split("\\s+");
        return nameParts[0];
    }

    /*
     * @description: Get last name
     * */
    public static String getLastName(String fullName) {
        if (isNullOrEmpty(fullName)) return "";

        String[] nameParts = fullName.trim().split("\\s+");
        return nameParts[nameParts.length - 1];
    }

    /*
     * @description: Convert number to currency
     * */
    public static String numberToCurrency(Long number, String prefix, String suffix) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        String currencyStr = format.format(number).replace("VND", "");

        StringBuilder result = new StringBuilder();

        if (!isNullOrEmpty(prefix)) {
            result.append(prefix).append(" ");
        }

        result.append(currencyStr);

        if (!isNullOrEmpty(suffix)) {
            result.append(" ").append(suffix);
        }

        return result.toString();
    }

    /*
     * @description: Set field data
     * */
    @SafeVarargs
    private static void setFieldData(
            Tuple sourceItem, String sourceFieldName, Field targetFieldItem, Object target,
            Map<String, String> dateFormats, Map<String, Object>... options
    ) throws IllegalAccessException {
        Integer digitsRound = null;

        if (options.length > 0) {
            Map<String, Object> option = options[0];

            digitsRound = isNullObject(option.get(CONVERT_QUERY_RESULT_OPTIONS.DIGITS_ROUND))
                    ? null : safeToInt(option.get(CONVERT_QUERY_RESULT_OPTIONS.DIGITS_ROUND));
        }

        targetFieldItem.setAccessible(true);
        Class<?> targetFieldType = targetFieldItem.getType();
        String targetFieldTypeName = targetFieldType.getName();

        String dateFormat;
        if (dateFormats.size() == 1) {
            dateFormat = dateFormats.get("ALL");
        } else {
            dateFormat = dateFormats.get(sourceFieldName);
        }

        Object sourceItemData = sourceItem.get(sourceFieldName);
        if (sourceItemData == null) return;

        DataTypeClassName targetFieldClassName = DataTypeClassName.get(targetFieldTypeName);
        switch (targetFieldClassName) {
            case UUID:
                targetFieldItem.set(target, safeToUUID(sourceItemData));
                break;
            case STRING:
                if (sourceItemData instanceof Date) {
                    targetFieldItem.set(target, dateToString(safeToDate(sourceItemData), dateFormat));
                } else {
                    targetFieldItem.set(target, safeToString(sourceItemData));
                }
                break;
            case LONG:
            case PRIMITIVE_LONG:
                targetFieldItem.set(target, safeToLong(sourceItemData));
                break;
            case DOUBLE:
            case PRIMITIVE_DOUBLE:
                if (isNullOrZero(digitsRound)) {
                    targetFieldItem.set(target, safeToDouble(sourceItemData));
                } else {
                    targetFieldItem.set(target, round(safeToDouble(sourceItemData), digitsRound));
                }
                break;
            case BOOLEAN:
            case PRIMITIVE:
                targetFieldItem.set(target, "true".equalsIgnoreCase(safeToString(sourceItemData)) || "1".equalsIgnoreCase(safeToString(sourceItemData)));
                break;
            case DATE:
                targetFieldItem.set(target, safeToDate(sourceItemData));
                break;
            case BIG_DECIMAL:
                targetFieldItem.set(target, safeToBigDecimal(sourceItemData));
                break;
            case INTEGER:
            case INT:
                targetFieldItem.set(target, safeToInt(sourceItemData));
                break;
            default:
        }
    }

    /*
     * @description: Convert Tuple to Object
     * */
    @SafeVarargs
    public static <T> List<T> tupleToObject(
            List<Tuple> listSource, Class<T> classTarget, Map<String, String> dateFormats, Map<String, Object>... options
    ) {
        try {
            List<T> result = new ArrayList<>();

            for (Tuple sourceItem : listSource) {

                Constructor<?> cons = classTarget.getConstructor();
                Object target = cons.newInstance();

                var targetFields = getAllFields(classTarget);

                for (Field targetFieldItem : targetFields) {
                    String fieldName = targetFieldItem.getName().toLowerCase();
                    List<TupleElement<?>> sourceFields = sourceItem.getElements();

                    try {
                        List<TupleElement<?>> collect = sourceFields.stream().filter(item -> {
                            String sourceFieldNameRemoveUnderscore = item.getAlias().replace("_", "");

                            return fieldName.equalsIgnoreCase(sourceFieldNameRemoveUnderscore);
                        }).toList();
                        String sourceFieldName = isNullOrEmpty(collect) ? "" : collect.get(0).getAlias();

                        setFieldData(sourceItem, sourceFieldName, targetFieldItem, target, dateFormats, options);
                    } catch (Exception e) {
                        String message = e.getMessage();

                        if (!safeEqual(message, "Unknown alias []")) log.error(message);
                    }
                }

                result.add((T) target);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        }
    }

    private static List<Field> getAllFields(Class clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
        result.addAll(filteredFields);
        return result;
    }

    /*
     * @description: Convert Tuple to Object
     * */
    @SafeVarargs
    public static <T> List<T> tupleToObject(
            List<Tuple> listSource, Class<T> classTarget, String dateFormat, Map<String, Object>... options
    ) {
        Map<String, String> dateFormats = new HashMap<>();
        dateFormats.put("ALL", dateFormat);

        return tupleToObject(listSource, classTarget, dateFormats, options);
    }

    /*
     * @description: Convert Tuple to Object
     * */
    @SafeVarargs
    public static <T> List<T> tupleToObject(
            List<Tuple> listSource, Class<T> classTarget, Map<String, Object>... options
    ) {
        return tupleToObject(listSource, classTarget, DATE_FORMAT, options);
    }

    /*
     * @description: Convert Tuple to Object
     * */
    public static <T> T tupleToObject(Tuple source, Class<T> classTarget, Map<String, String> dateFormats) {
        try {
            Constructor<?> cons = classTarget.getConstructor();
            Object target = cons.newInstance();

            Field[] targetFields = target.getClass().getDeclaredFields();

            for (Field targetFieldItem : targetFields) {
                String fieldName = targetFieldItem.getName().toLowerCase();
                List<TupleElement<?>> sourceFields = source.getElements();

                try {
                    List<TupleElement<?>> collect = sourceFields.stream().filter(item -> {
                        String sourceFieldNameRemoveUnderscore = item.getAlias().replace("_", "");

                        return fieldName.equalsIgnoreCase(sourceFieldNameRemoveUnderscore);
                    }).collect(Collectors.toList());
                    if (isNullOrEmpty(collect)) continue;

                    String sourceFieldName = collect.get(0).getAlias();

                    setFieldData(source, sourceFieldName, targetFieldItem, target, dateFormats);
                } catch (Exception e) {
                    String message = e.getMessage();

                    if (!safeEqual(message, "Unknown alias []")) log.error(message);
                }
            }

            return ((T) target);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    /*
     * @description: Convert Tuple to Object
     * */
    public static <T> T tupleToObject(Tuple source, Class<T> classTarget, String dateFormat) {
        Map<String, String> dateFormats = new HashMap<>();
        dateFormats.put("ALL", dateFormat);

        return tupleToObject(source, classTarget, dateFormats);
    }

    /*
     * @description: Convert Tuple to Object
     * */
    public static <T> T tupleToObject(Tuple source, Class<T> classTarget) {
        return tupleToObject(source, classTarget, DATE_FORMAT);
    }

    /*
     * @description: Convert json to object
     * */
    public static <T> T jsonToObject(String json, Class<T> classTarget) {
        try {
            if (isNullOrEmpty(json)) return null;

            return Singleton.getObjectMapper().readValue(json, classTarget);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @description: Convert json to list object
     * */
    public static <T> List<T> jsonToListObject(String json, Class<T[]> classTarget) {
        try {
            if (isNullOrEmpty(json)) return Collections.emptyList();

            return Arrays.asList(Singleton.getObjectMapper().readValue(json, classTarget));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /*
     * @description: Increment Code
     * */
    public static String incrementCode(String code, String prefix, Integer indexLength) {
        code = safeToString(code);
        prefix = safeToString(prefix);

        code = code.replace(prefix, "");
        Integer index = safeToInt(code);
        Integer nextIndex = index + 1;
        int nextIndexLength = safeToString(nextIndex).length();
        if (nextIndexLength > indexLength) {
            return String.format(
                    "%s%d",
                    prefix,
                    nextIndex
            );
        }

        return String.format(
                "%s%s%s",
                prefix,
                "0".repeat(indexLength - nextIndexLength),
                nextIndex
        );
    }

    public static String incrementCode(String code, String prefix, String suffix, Integer indexLength) {
        code = safeToString(code);
        prefix = safeToString(prefix);
        suffix = safeToString(suffix);

        code = code.replace(prefix, "").replaceAll(suffix, "");
        Integer index = safeToInt(code);
        Integer nextIndex = index + 1;
        int nextIndexLength = safeToString(nextIndex).length();
        if (nextIndexLength > indexLength) {
            return String.format(
                    "%s%d%s",
                    prefix,
                    nextIndex,
                    suffix
            );
        }

        StringBuilder result = new StringBuilder(prefix);
        for (int i = 0; i < indexLength - nextIndexLength; i++) {
            result.append("0");
        }

        result.append(nextIndex);
        result.append(suffix);

        return result.toString();
    }

    /*
     * @description: Generate code
     * */
    public static String generateCode(String prefix, String suffix, Integer maxIndexLength, Integer startIndex) {
        prefix = safeToString(prefix);
        suffix = safeToString(suffix);

        String format = "%s%0" + maxIndexLength + "d%s";
        return String.format(format, prefix, startIndex, suffix);
    }

    /*
     * @description: Update code
     * */
    public static String updateCode(
            String code, String oldPrefix, String oldSuffix,
            String newPrefix, String newSuffix, Integer newIndexLength
    ) {
        code = safeToString(code);
        oldPrefix = safeToString(oldPrefix);
        oldSuffix = safeToString(oldSuffix);
        newPrefix = safeToString(newPrefix);
        newSuffix = safeToString(newSuffix);
        newIndexLength = safeToInt(newIndexLength);

        code = code.replace(oldPrefix, "").replaceAll(oldSuffix, "");
        Integer currentIndex = safeToInt(code);
        Integer currentIndexLength = safeToString(currentIndex).length();
        if (currentIndexLength >= newIndexLength) {
            return String.format(
                    "%s%d%s",
                    newPrefix,
                    currentIndex,
                    newSuffix
            );
        }

        StringBuilder result = new StringBuilder(newPrefix);
        for (int i = 0; i < newIndexLength - currentIndexLength; i++) {
            result.append("0");
        }

        result.append(currentIndex);
        result.append(newSuffix);

        return result.toString();
    }

    /*
     * @description: Convert time unit
     * */
    public static Long convertTimeUnit(Long sourceTime, TimeUnit srcUnit, TimeUnit desUnit) {
        return srcUnit.convert(sourceTime, desUnit);
    }

    /*
     * @description: Copy properties to new object
     * */
    public static <T> T copyProperties(Object source, Class<T> classTarget) {
        try {
            Constructor<?> cons = classTarget.getConstructor();
            Object target = cons.newInstance();


            BeanUtils.copyProperties(source, target);
            return (T) target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @description: Copy properties to new object ignore properties
     * */
    public static <T> T copyProperties(Object source, Class<T> classTarget, String... ignoreProperties) {
        try {
            Constructor<?> cons = classTarget.getConstructor();
            Object target = cons.newInstance();


            BeanUtils.copyProperties(source, target, ignoreProperties);
            return (T) target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @description: Get field value in object
     * */
    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Class<?> myClass = obj.getClass();
        Field field = myClass.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(obj);
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String removeExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String generateUniqueFileName(String fileName) {
        String slug = toSlug(fileName, true);

        return String.format("%s-%s", slug, safeToString(System.currentTimeMillis()));
    }
}