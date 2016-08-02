package com.ahoy.parser.cron;

import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.dao.ParseDataDao;
import com.ahoy.parser.dao.ParseDataDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Cron {
	
	Logger logger = LoggerFactory.getLogger(Cron.class);
	
	public String url;	
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void hitApi(){
		
		String resp = this.httpres(url);
		logger.info("[Cron][hitApi] url: "+url+" | res: "+resp);
	}
	
	public void shortMessage(){
		try{
			
			URL propUrl = Cron.class.getResource("/parser.properties");
			Properties properties = new Properties();
			properties.load(propUrl.openStream());			
			String sendTo = properties.getProperty("sendto");
			String sendFrom = properties.getProperty("sendfrom");
			
			logger.info("[Cron][shortMessage] sendTo: "+sendTo+" | sendFrom: "+sendFrom);	
			
			if(sendTo!=null && sendTo.trim().matches("[0-9,]+") && sendFrom!=null && !sendFrom.equals("")&&sendFrom.length()==6){
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = format.format(new Date());
				 
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDate = format.parse(todayDate+" 00:00:00");
				Date endDate = format.parse(todayDate+" 23:59:59");
				
				List<MerchantDo> merchantDos = new MerchantDaoImpl().getByType((short)1);
				ParseDataDao parseDataDao = new ParseDataDaoImpl();
				StringBuilder builder = new StringBuilder();
				
				for (MerchantDo merchantDo : merchantDos) {
					builder.append((merchantDo.getMerchantName().substring(0, 1).toUpperCase()+merchantDo.getMerchantName().substring(1))+"[");
					Set<CityDo> cityDos = merchantDo.getCityDos();
					for (CityDo cityDo : cityDos) {
						long newCount = parseDataDao.getCount("createdOn", cityDo.getCityId(), merchantDo.getMerchantId(), startDate, endDate);
						long oldCount = parseDataDao.getCount("updatedOn", cityDo.getCityId(), merchantDo.getMerchantId(), startDate, endDate);
						builder.append((cityDo.getCityName().substring(0, 1).toUpperCase()+cityDo.getCityName().substring(1))+"(New:" +newCount+", Old:"+oldCount+"),");
					}					
					builder.append("] ");					
				}
				
				if(builder!=null && builder.length()>0){
					if(sendTo.trim().contains(",")){
						String[] splt = sendTo.split(",");
						for (String mobile : splt) {
							String msgPush = "http://bulk.netappleindia.com/sendsms/bulksms?username=ntap-Ahoy&password=ahoy12&type=0&dlr=1&destination="+mobile+"&source="+sendFrom.toUpperCase()+"&message="+URLEncoder.encode(builder.toString(),"UTF-8");
//							String msgPush ="http://websms.one97.net/sendsms/sms_request.php?username=google&password=go09le&smsfrom="+sendFrom.toUpperCase()+"&receiver="+mobile+"&content="+URLEncoder.encode(builder.toString(),"UTF-8");
							String resp = this.httpres(msgPush);
							logger.info("[Cron][shortMessage] msisdn: "+mobile+" | response message is: \""+builder+"\" resp: "+resp);
						}
					}else{
						String msgPush = "http://bulk.netappleindia.com/sendsms/bulksms?username=ntap-Ahoy&password=ahoy12&type=0&dlr=1&destination="+sendTo+"&source="+sendFrom.toUpperCase()+"&message="+URLEncoder.encode(builder.toString(),"UTF-8");
						
//						String msgPush ="http://websms.one97.net/sendsms/sms_request.php?username=google&password=go09le&smsfrom="+sendFrom.toUpperCase()+"&receiver="+sendTo+"&content="+URLEncoder.encode(builder.toString(),"UTF-8");
						String resp = this.httpres(msgPush);
						logger.info("[Cron][shortMessage] msisdn: "+sendTo+" | response message is: \""+builder+"\" resp: "+resp);
					}
				}else{
					logger.info("[Cron][shortMessage] response message is: \""+builder+"\"");
				}
			}		
			
		}catch (Exception e) {
			logger.error("[Cron][shortMessage] Exception: "+e);
		}
	}
	
	private String httpres(String httpurl){
		URL url = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			url = new URL(httpurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(25*1000);
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			
			if(stringBuilder!=null && stringBuilder.toString().equals(""))
				stringBuilder.append("Success");
		}catch (Exception e) {
			if(e.getMessage().equalsIgnoreCase("Read timed out")){
				stringBuilder.append("Success");
			}else{
				logger.error("[Cron][httpres]"+httpurl+"\n Exception: " + e);
			}
		} finally {
			
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return stringBuilder.toString();
	} 
}
