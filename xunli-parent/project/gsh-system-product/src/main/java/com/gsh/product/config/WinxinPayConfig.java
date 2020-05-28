package com.gsh.product.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants.SignType;

/**
 * 扫码支付参数配置，可用于支付和退款的参数
 * @author LCS
 *
 */
public class WinxinPayConfig implements WXPayConfig {

	private byte[] certData;
	
	public void setCertData(byte[] certData) {
		this.certData = certData;
	}
	
	/**
	 * 应用App的ID
	 */
	@Override
	public String getAppID() {
		return "wx4cde1725705b253b";
	}

	/**
	 * 商户ID
	 */
	@Override
	public String getMchID() {
		return "1586428711";
	}
	
	/**
	 * API密钥Key
	 */
	@Override
	public String getKey() {
		return "DB6415D143FC0C92EF43EB5633AC261F";
	}

	@Override
	public InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
	}

	@Override
	public int getHttpConnectTimeoutMs() {
		// TODO Auto-generated method stub
		return 10*1000;
	}

	@Override
	public int getHttpReadTimeoutMs() {
		// TODO Auto-generated method stub
		return 20*1000;
	}
	
	public SignType getSignType(){
		return SignType.HMACSHA256;
	}

}
