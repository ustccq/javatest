package ustccq.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainLand {
	private static HashSet<String> optionSet = new HashSet<String>();
	private static HashMap<String, String> optionMap = new HashMap<String, String>();
	
	public static Boolean isInteger(String str) {
		
		Pattern pattern = Pattern.compile("[1-9]{1}[0-9]*");
	    return pattern.matcher(str).matches();
	}
	
	public static String nothingIsImportant(String targetStr) {
		System.err.println(targetStr);
		return targetStr + " OK";
	}
	
	public static String getUnixtime() {
		long milli = System.currentTimeMillis()/1000;
		return String.valueOf(milli);
	}
	
	public static String getUnixtimeInMillisec() {
		long milli = System.currentTimeMillis();
		return String.valueOf(milli);
	}
	
	public static String convertMultiFuncInputBnd(String input) {
		if (null == input || input.isEmpty())
			return input;
		else {
			System.err.println("convertMultiFuncInput:" + input);
			if (input.contains(ContextConstant.INPUT_CONVERTER_PATTERN_PREFIX)
					&& input.contains(ContextConstant.INPUT_CONVERTER_PATTERN_SUFFIX)) {
				String pattern = "\\u0024\\u007B(((lettermix|letterlowercase|lettercapital|letterintmix|int|double|chinese|specialsymbol|timeCurrent)\\u002Erandom\\u0028\\d*)|(unixtime(InMilliseconds){0,1}\\u0028))\\u0029\\u007D";
				Pattern p = Pattern.compile(pattern);
				Matcher match = p.matcher(input);
				StringBuffer sb = new StringBuffer();

				while (match.find()) {
					String matchedSubString = match.group(0);
					String convert = convertFuncInput(matchedSubString);
					if (matchedSubString.contains("specialsymbol")) {
						convert = convert.replaceAll("\\u0024", "\\\\\\$");
					}
					System.out.println(convert);
					match.appendReplacement(sb, convert);

				}
				match.appendTail(sb);// 从截取点将后面的字符串接上
				String convertedInput = sb.toString();
				System.out.println(convertedInput);
				return convertedInput;
			}
		}
		return input;
	}
	
	public static String convertMultiFuncInput(String input){
		if (null == input || input.isEmpty())
			return input;
		else{
			if (input.contains(ContextConstant.INPUT_CONVERTER_PATTERN_PREFIX)
					&& input.contains(ContextConstant.INPUT_CONVERTER_PATTERN_SUFFIX))
			{
				String pattern = "\\u0024\\u007B(((lettermix|letterlowercase|lettercapital|letterintmix|int|double|chinese|specialsymbol|timeCurrent)\\u002Erandom\\u0028\\d*)|(unixtime(InMilliseconds){0,1}\\u0028))\\u0029\\u007D";
				Pattern p = Pattern.compile(pattern);
				Matcher match = p.matcher(input);
				StringBuffer sb = new StringBuffer();
				
				while (match.find()) {
					String matchedSubString = match.group(0);
					String convert = convertFuncInput(matchedSubString);
					if (matchedSubString.contains("specialsymbol")){
						convert = convert.replaceAll("\\u0024", "\\\\\\$");
					}
					System.out.println(convert);
					match.appendReplacement(sb, convert);
				}
//				
//				//非随机函数
//				if (!bRandomFunc) {
//					pattern ="\\u0024\\u007Bunixtime(InMilliseconds){0,1}\\u0028\\u0029\\u007D";
//					p = Pattern.compile(pattern);
//					match = p.matcher(input);
//					sb = new StringBuffer();
//					
//					while (match.find()) {
//						String matchedSubString = match.group(0);
//						String convert = convertFuncInput(matchedSubString);
//						System.out.println(convert);
//						match.appendReplacement(sb, convert);
//					}
//				}
				match.appendTail(sb);//从截取点将后面的字符串接上 
				String convertedInput = sb.toString();
				
				System.out.println("convertedInput:" + convertedInput);
				return convertedInput;
			}
		}
		return input;
	}
	
	public static String convertFuncInput(String input){
		do{
			if (null == input || input.isEmpty())
				break;
			else{
				//match the ${int.random()}
				if (input.startsWith(ContextConstant.INPUT_CONVERTER_PATTERN_PREFIX) 
						&& input.endsWith(ContextConstant.INPUT_CONVERTER_PATTERN_SUFFIX)){
					
					if (!input.contains("random") && !input.contains("unixtime"))
						break;
					
					int prefixLen = ContextConstant.INPUT_CONVERTER_PATTERN_PREFIX.length();
					int suffixLen = ContextConstant.INPUT_CONVERTER_PATTERN_SUFFIX.length();
					if (prefixLen + suffixLen >= input.length())
						break;
					
					//get the function name like: int.random()
					String functionName = input.substring(prefixLen, input.length() - suffixLen);
					if (functionName.contains("()")){
						int baseT = 8;
						if (functionName.equals(ContextConstant.INPUT_CONVERTER_TIME_CURRENT)){
							baseT = 14;
						}
						
						int nLen = Math.abs(new Random().nextInt() % baseT);
						nLen++;
						String content = genContent(functionName, nLen);
						if (content.isEmpty())
							break;
						else
							return content;
					}else if (functionName.contains("(") && functionName.contains(")")){
						int posL = functionName.indexOf("(");
						int posR = functionName.indexOf(")");
						
						if (posR > posL && posL > 0){
							String funcName = functionName.substring(0, posL);
							String number = functionName.substring(posL + 1, posR);
							if (number.matches("[1-9]{1}\\d*") && number.length() <= 5){
								int nLen = new Integer(number);
								String content = genContent(funcName + "()", nLen);
								if (content.isEmpty())
									break;
								else
									return content;
							}else{
								break;
							}
						}else{
							break;
						}
					}
					
				}else{
					break;
				}
			}
		}while(false);
		return input;
	}
	
	protected static String genContent(String funcName, int nLen){
		switch(funcName){
		case ContextConstant.INPUT_CONVERTER_PATTERN_INT:
			return getRandomInt(nLen);
		case ContextConstant.INPUT_CONVERTER_PATTERN_DOUBLE:
			return doubleRandom(nLen);
		case ContextConstant.INPUT_CONVERTER_PATTERN_CHINESE:
			return chineseRandom(nLen);
		case ContextConstant.INPUT_CONVERTER_PATTERN_LETTER:
			return getLetterMixed(nLen);
		case ContextConstant.INPUT_CONVERTER_PATTERN_LETTER1:
			return getLittleLetter(nLen);
		case ContextConstant.INPUT_CONVERTER_PATTERN_LETTER2:
			return getCapitalLetter(nLen);
		case ContextConstant.INPUT_CONVERTER_PATTERN_LETTERINTMIX:
			return getLetterIntMixed(nLen);
		case ContextConstant.INPUT_CONVERTER_PATTERN_SPECIALSYMBOL:
			return getSpecialSymbol(nLen);
		case ContextConstant.INPUT_CONVERTER_TIME_CURRENT:
			return getCurrentTime(nLen);
		case ContextConstant.INPUT_CONVERTER_UNIXTIME:
			return getUnixtime();
		case ContextConstant.INPUT_CONVERTER_UNIXTIME_MILLISEC:
			return getUnixtimeInMillisec();
		default:
			return "";
	}
	}
	
	public static String getLittleLetter(int length){
		return innerGetAssignedCharByLength("abcdefghijklmnopqrstuvwxyz", length);
	}
	
	public static String getCapitalLetter(int length){
		return innerGetAssignedCharByLength("ABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
	}
	
	public static String getLetterMixed(int length){
		return innerGetAssignedCharByLength("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
	}
	
	public static String getLetterIntMixed(int length){
		return innerGetAssignedCharByLength("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
	}
	
    public static String getRandomInt(int length) {
    	return innerGetAssignedCharByLength("0123456789", length);
    }
    
    public static String getSpecialSymbol(int length) {
    	return innerGetAssignedCharByLength("!@#$%^&*", length);
    }
    
    protected static String innerGetAssignedCharByLength(String source, int length){
		if (length <= 0)
    		return "";
		if (null == source || source.isEmpty())
			return "";
    	
		String base = source;//;
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {     
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));     
		}     
		return sb.toString();
	}
	
	/**
	 * 
	 * @param len
	 * @return
	 */
	public static String doubleRandom(int len){
		//TODO
		double randomD = new Random().nextDouble();
		return String.format("%f", randomD);
	}
	
	/**
	 * 
	 * @param len
	 * @return
	 */
	public static String chineseRandom(int len){
		String result = "";
		if (len < 100){
			try{
				while(len >0){
					len--;
					result += chineseCreate();
				}
			}catch(Exception e){
				result = "";
			}
		}else{
			return "";
		}
		return result;
	}
	
	/**
	 * 获取系统当前时间字符，格式例如：20180712200315
	 * len不支持输入0,大于14则返回值也只为14位
	 * @return
	 */
	public static String getCurrentTime(int len) {
		String result = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		result = df.format(new Date());
		if(len > 0  && len <= 14){
			result = result.substring(0, len);
		}else {
			result = result.substring(0, 14);
		}		
		return result;		
	}
	
	public static String chineseCreate() throws Exception {
		String str = null;
		int hightPos, lowPos; // 定义高低位
		Random random = new Random();
		hightPos = (176 + Math.abs(random.nextInt(39)));//获取高位值
		lowPos = (161 + Math.abs(random.nextInt(93)));//获取低位值
		byte[] b = new byte[2];
		b[0] = (new Integer(hightPos).byteValue());
		b[1] = (new Integer(lowPos).byteValue());
		str = new String(b, "GBK");//转成中文
		return str;
	}
	
	public static void main(String[] args) {
		
		System.out.println(convertMultiFuncInputBnd("${unixtime()}"));
		System.out.println(convertMultiFuncInput("${unixtime()}${chinese.random(5)}${unixtimeInMilliseconds()}${double.random(10)}"));
		System.out.println(convertMultiFuncInput("${unixtimeInMilliseconds()}"));
		System.out.println(convertMultiFuncInput("${double.random(10)}"));
		System.out.println(convertMultiFuncInput("${lettercapital.random(5)}"));
		 // 精确到毫秒
        // 获取当前时间戳
		System.out.println(getUnixtime());
		System.out.println(getUnixtimeInMillisec());
        System.out.println(System.currentTimeMillis());
        System.out.println(Calendar.getInstance().getTimeInMillis());
        System.out.println(new Date().getTime());

        // 精确到秒
        // 获取当前时间戳
        System.out.println(System.currentTimeMillis() / 1000);
        System.out.println(Calendar.getInstance().getTimeInMillis() / 1000);
        System.out.println(new Date().getTime() / 1000);

        // 精确到毫秒
        // 获取指定格式的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        // 输出字符串
        System.out.println(df.format(new Date()));
        // 获取指定时间Date对象，参数是时间戳，只能精确到秒
        System.out.println(new Date(1510369871));
        df.getCalendar();
        // 获取指定时间的时间戳
        try {
            System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").parse("2017/11/11 11:11:11:111").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
		
		
		String aaa = "she is laji";
		boolean bYes = new Random().nextBoolean();
		aaa += bYes ? " definitly" : " for sake!";
		System.err.println(aaa);
		nothingIsImportant(aaa = nothingIsImportant(aaa));
		
		System.out.println(aaa);
		
		String tmpStr = String.format("%s%n%s", "岁月是把杀猪刀.", "志玲是个老明星!");
		System.err.println(tmpStr);
		
		String string = String.format("%s 123 %s ddd %d", "money", "people", 243);
		System.out.println(string);
		
		Date date = new Date();
		String str = String.format("%tc %n %<tF %n %<tD",date);
		System.out.println(str);
		
		String str1 = String.format("%1$-6s%2$s %3$#6o", "Hello","World!", 0xFF);
		System.out.println(str1);
		
		
		String str2 = String.format("%1$04d %2$(,d",1,-1000);
		System.out.println(str2);
		
		Set<String> tmpSet = new HashSet<String>();
		
		tmpSet.add("nothing1");
		tmpSet.add("nothing2");
		tmpSet.add("nothing3");
		
		Map<String, String> tmpHash = new HashMap<String, String>();
		tmpHash.put("k1", "v1");
		tmpHash.put("k2", "v2");
		tmpHash.put("k3", "v3");
		
		System.out.println(tmpSet.toString() + "\r\n" + tmpHash.toString());
		
		String txt1 = "ERROR";
		String txt2 = "ERROR";
		String txt3 = new String("ERROR");
		String txt4 = new String("    ERROR     ");
		txt4 = txt4.trim();
		
		System.out.println(txt1.equals(txt2));
		System.out.println(txt1.equals(txt3));
		System.out.println(txt3.equals("ERROR"));
		System.out.println(txt4.equals(txt1));
		
		System.out.println(optionSet.add("nothing"));
		System.out.println(optionSet.add("nothing"));
		System.out.println(optionSet.add("nothing"));
		
		System.out.println(optionMap.put("nothing", "seek peace"));
		System.out.println(optionMap.put("nothing", "seek peace"));
		System.out.println(optionMap.put("nothing", "seek peace"));
		
		String option = "nothing: ";
		String[] optionItems = option.split(":");
		for(String item : optionItems) {
			if (item.isEmpty())
				System.out.println("item is empty string");
			else
				System.out.println(item);
		}
		System.err.println("length:" + optionItems.length);
		
		if (true)
			return;
		
		System.out.println(isInteger("011111111111111"));		
		System.out.println(isInteger("1320"));		
		System.out.println(isInteger("12300001111111111111111111111"));		
		System.out.println(isInteger("123"));		
	}
	
	static class ContextConstant {
		public static final String ALL = "ALL";

	    public static final int TAB_NAME_ROW_ID = 0;

	    //for element option
	    public static final String ELEMENT_REPLACE_LOCATOR_VALUE			= "ELE_REPLACE_LOCATOR_VALUE";
//	    public static final String ELEMENT_REPLACE_DATA						= "ELE_REPLACE_DATA";

	    //new format with name, type, id or css
	    public static final int OBJECT_TYPE_ROW_ID 							= 1;
	    public static final int OBJECT_LOCATOR_TYPE_ROW_ID					= 2;
	    public static final int OBJECT_LOCATOR_STRING_ROW_ID				= 3;
	    //instruction options
	    public static final String RESULT_IGNORE 							= "RES_IGNORE";//"RESULT_IGNORE";
	    public static final String INSTRUCTION_IGNORE 						= "INS_IGNORE";//"IGNORE";
	    public static final String INSTRUCTION_STOP 						= "SYS_STOP";//"STOP";
	    public static final String INSTRUCTION_INGORE_DISABLED_BUTTON 		= "BTN_IGNORE_DISABLED";//"IGNORE_DISABLED_BUTTON";
	    public static final String INSTRUCTION_BUTTON_UNTIL_DISABLE 		= "BTN_CLICK_UNTIL_DISABLE";//BUTT_UNTIL_DISABLE
	    public static final String INSTRUCTION_BUTTON_UNTIL_DISAPPEARS 		= "BTN_CLICK_UNTIL_DISAPPEARS";//"BUTT_UNTIL_DISAPPEARS";
	    public static final String INSTRUCTION_BUTTON_UNTIL_POPUP 			= "BTN_CLICK_UNTIL_POPUP";//"BUTT_UNTIL_POPUP";
	    public static final String INSTRUCTION_BUTTON_RANDOM_CLICK_ONE 		= "BTN_RANDOM_CLICK_ONE";//"BUTT_RANDOM_CLICK_ONE";
//	    public static final String INSTRUCTION_BUTTON_RANDOM_CLICK 			= "BTN_RANDOM_CLICK";//"BUTT_RANDOM_CLICK";
//	    public static final String INSTRUCTION_VERIFY_FAILED_EXIT 			= "VERIFY_FAILED_EXIT";


	    public static final String INSTRUCTION_BUTTON_AlERT_OK				= "BTN_AlERT_CLICK_OK";//"BUTTON_AlERT_OK";
	    public static final String BTN_CLICK_UNTIL_VERIFY_ALERT_TEXT		= "BTN_CLICK_UNTIL_VERIFY_ALERT_TEXT";
	    public static final String LNK_CLICK_UNTIL_VERIFY_ALERT_TEXT		= "LNK_CLICK_UNTIL_VERIFY_ALERT_TEXT";
	    public static final String INSTRUCTION_BUTTON_AlERT_CANCEL			= "BUTTON_AlERT_NO";
	    public static final String INSTRUCTION_BUTTON_AlERT_INFO			= "BUTTON_AlERT_INFO";
	    public static final String INSTRUCTION_BUTTON_FIND_TEXT				= "BUTTON_FIND_TEXT";
	    public static final String INSTRUCTION_LINK_FIND_TEXT				= "LINK_FIND_TEXT";
	    public static final String INSTRUCTION_LINK_AlERT_INFO				= "LINK_AlERT_INFO";

	    //compare returnValue with optionValue
	    //compare like DTA_COMPARE_RETURN_VALUE:expectValue
	    public static final String OPTION_COMPARE_RETURN_VALUE				= "DTA_COMPARE_RETURN_VALUE";//"COMPARE_RETURN_VALUE";

	    //save it like
	    //SAVE_INPUT:KEY1
	    //and use it like $(KEY1)
	    public static final String OPTION_SAVE_INPUT						= "DTA_SAVE_INPUT";//"SAVE_INPUT";
	    public static final String OPTION_SAVE_TEXT							= "DTA_SAVE_TEXT";//"SAVE_TEXT";
	    public static final String INSTRUCTION_FAIL_IGNORE_START 			= "RES_IGNORE_FAIL_START";//"FAIL_IGNORE_START";
	    public static final String INSTRUCTION_FAIL_IGNORE_END 				= "RES_IGNORE_FAIL_END";//"FAIL_IGNORE_END";
	    public static final String INSTRUCTION_NO_RADIO_VERIFY 				= "OPT_NO_RADIO_VERIFY";//"NO_RADIO_VERIFY";
	    public static final String INSTRUCTION_NO_VERIFY 					= "NO_VERIFY";//"NO_VERIFY";
	    //special key works
	    //TODO
	    public static final String IGNORE 									= "IGNORE";//"!IGNORE!";
	    public static final int POPUP_WAIT_MAX 								= 10;
	    public static final int WEBELEMENT_WAIT_MAX 						= 20;
	    public static final int WEBELEMENT_RETRY_MAX 						= 5;
	    public static final String TIME_FORMAT 								= "yyyy-MM-dd";
		//Globe unique string
		private static final String UUID_TIME_FORMAT 						= "MMddHHmmssSSS";
		public static String UUID 											= "";

		//keywords for data input
		public static final String DATA_STAMP 								= "!DateStamp!";

		//info constants
		public static final String SKIP_BY_NOT_FOUND 						= "SkipByNotFound";

	    // table selection options
		public static final String TABLE_MODIFY 							= "Modify";
		public static final String TABLE_ADD 								= "Add";
	    public static final String TABLE_FIRST_ITEM_ALL_FIELDS_NON_EMPTY 	= "FirstItemAllFieldsNonEmpty";
		public static final String TABLE_FIRST_ITEM 						= "FirstItem";
		public static final String TABLE_BRANCH_LOCATION 					= "Branch Location";
		public static final String TABLE_MAILING_ADDRESS 					= "Mailing Address";
		public static final HashSet<String> LOCATION_LIST = new HashSet<String>(Arrays.asList(new String[]{
				TABLE_BRANCH_LOCATION,
				TABLE_MAILING_ADDRESS
				}));
		public static final Hashtable<String, String> LOCATION_MAPPING = new Hashtable<String, String>() {{
						put(TABLE_BRANCH_LOCATION, "(Branch Location)");
						put(TABLE_MAILING_ADDRESS, "(Account General Mailing)");
				}};


		//directory valuables
		public static String SYSTEM_ROOT_DIRECTORY 				= "";

	    //runtime valuables for test
		public static String TEST_CASE_RESULT_FOLDER 			= "";
		public static String REMOTE_TEST_CASE_RESULT_FOLDER		= "";
		public static String REMOTE_INSTRUCTION_RESULT_FOLDER	= "";
	    public static String LOG_FOLDER 						= "";
	    public static String CURRENT_FOLDER 					= "";

	    //TestCase valuables
		public static int TEST_CASE_ID 							= 0;
		public static int TEST_CASE_STEP_ID 					= 0;
		public static String TEST_CASE_NAME 					= "";
	    public static int EXCEL_ROW_NUMBER 						= 0;
	    public static String EXCEL_ROW_NUMBER_STRING 			= "";
	    public static String INSTRUCTION 						= "";
	    public static String INPUT 								= "";

	    public static final String INPUT_CONVERTER_PATTERN_PREFIX	= "${";
	    public static final String INPUT_CONVERTER_PATTERN_INT		= "int.random()";
	    public static final String INPUT_CONVERTER_PATTERN_DOUBLE	= "double.random()";
	    public static final String INPUT_CONVERTER_PATTERN_CHINESE	= "chinese.random()";
	    public static final String INPUT_CONVERTER_PATTERN_LETTER	= "lettermix.random()";
	    public static final String INPUT_CONVERTER_PATTERN_LETTER1	= "letterlowercase.random()";
	    public static final String INPUT_CONVERTER_PATTERN_LETTER2	= "lettercapital.random()";
	    public static final String INPUT_CONVERTER_PATTERN_LETTERINTMIX	= "letterintmix.random()";
	    public static final String INPUT_CONVERTER_PATTERN_SPECIALSYMBOL= "specialsymbol.random()";
	    public static final String INPUT_CONVERTER_TIME_CURRENT		= "timeCurrent.random()";
	    public static final String INPUT_CONVERTER_UNIXTIME			= "unixtime()";
	    public static final String INPUT_CONVERTER_UNIXTIME_MILLISEC	= "unixtimeInMilliseconds()";
	    public static final String INPUT_CONVERTER_PATTERN_SUFFIX	= "}";
	    
	}
}
