package com.gsh.product.config;

/**
 * 扫码支付参数配置，可用于支付和退款的参数
 * @author LCS
 *
 */
public class AlipayConfig {
    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     * @return
     */
	public String getAppId() {
		return "2016102200736577";
	}
	
	/**
     * 商户私钥，您的PKCS8格式RSA2私钥
     * @return
     */
	public String getPrivateKey() {
		return "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCV07b0XDPiM73g90mwcVk5zDap+uNt4ryx9rVMej5bbJVEKqgCquqS1s8KE/l6l+r7zOHDa7GVtJiCf253pVcqOmPh30PPY+w9ORFLDTgzX9Vy89cnJeKeiFv51n38KaUp23qKuG3oDkD6jeAtYxnors6/nt506nxa6HLXj8MhmbQhfme5RHj4yk/M+prz9Fr2FO2yiPwKr1U/sCvnlefifrcxlFQJhQfG4kQZrmDzTRd+HhHdKHToei3652ep8xVPLEVZiUidGziC9fsIXJyzvqZ6cmW2ymDueZhSiQoN5/dbISq8Du4pPTsLbh5fGXHT84eFdMkVL/atT/fakym/AgMBAAECggEACDY6aNpE3bkIRzRGja0ovso6lMZzgKpUvUxQU4i/sjMZW0sbOkoz4Sh04JCHs/hvoK1yEkvsx8/GLCDf5R7i0CWzI8Yg6VUItbJTCFIJKoeuI0bKHBMvgJDouaDP9uoxa0HkTYYeQAHH/fOYveQYn3A1VLtYZgAUIMdsw/PVNhopDJqxmUej5VLo5sl2Fzy5+EB5PXomCWVWWHUncA0e4HAl1RB/NQ+EXZrKSiejZaaDB+CXyg8IqnxkqlWApJqUNSqt0c/r9omhVyeYCLzQwD753V3kP8GadrDWUi7vMrDlD/vP8c1EnDmDCO88ejX1ahZ29D7FeNb5i6KgFe3rAQKBgQDPOQM21JxrYTbEXklp1thw6SPPCrnbXAdu4wcsD8C5IGVohZRDJQb+HEUatCywdFaDP8A36AlFhmb5KVQn3UfhBts/7KsUuUsVr+VYcOcGDukSNNUYfVRHpcrEcPipzV/CztK9MXEiSztLaaixdCBHH5FCf51B+B05mB5Iv9fX0QKBgQC5GBuIafwROrnfH9edZL92hjYAlVKVT8T/CSsAnMglPVJyOGCwI97c1EujDrSbl+n8smuPE/599NCO/naHfS/+IsP616Xqcxf7/3h2GIoCCuliW94vLIA0D26c7k2RvWO7nMdOWloPnADZOLNda6epRqMxccNzL5JIlcCHJ2rcjwKBgQDOpoB6EZTb0NqZAZ4cmlDGaPl4461tAF25S1Y/roX3GEOI9I1iWG6cwvx3IO7gGWSdBtpd5lw4pUvTdk+iYZ3o3rmPCimiRRaHzh3dENqSxBPSQDWycmm1+bQ4KqEQVOsGjtLCMagtFvFOx1Ofg7scsnJd6n2c6B3c7cJlsktBwQKBgBqk58G5T/nC9GK0GLuldxI+u6NV22927iqwTSpnZ0BFFYsvpmt0nm43eTPduA31zk77+BO+kxHanfMXmkRitvfkVUXUqSOYsckqu1J0sdkdbP8lrlQoGcRkvE2G2E+0bZjiNqaGxxQcJtseGGPITXhGnNNPRw1bubkNJYvPFsjtAoGBALfabU8FnGMUcp4QxirMMUdPuyXacHm4drsoFvOEvfSs7y13iGk/ahMt0D6rs7hf2ycTMqxojh5d9FKKukDjzvzucr8eoCOQrxQE6KLDcKiezCr+rNquJJBYGdZL2y0IhLWpUVhvS3tR6wlCTGmEFjeoo4nRaY8+kh/m+GTdkt+b";
	}
	
	/**
     * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
     * @return
     */
	public String getAlipayPublicKey() {
		return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlXxLIaaZTGXIWF91LFINrm3ltCcEcj10ek7VzhU67MZNnun92UhnfHQavzxAsg7jQZoMh16kh4uTREvW7HsvrDSniDZtKdoKU/q1IYsqFESd/fS6yEgF+g7I48C8EH9tv7pJCw7GcHCEGNlGptzXpJ7YBB8p7MaMwEpA0tjr6nxr4zLngLddgiyrIzabhAi0R9w/ouNBcpa/ptr7p0ZLDoTp/9kSm2CTe80455clBdZmxamYHJnTfJzGCPvtSy1yEmUQPNU3LJD8q4aWFuRLwU7uJX4I/5dVBFAIcXgZ4cH2+SI9luQkIP4IbSu12rShQqEY1cidu6QtsDpk0qF23QIDAQAB/q1IYsqFESd/fS6yEgF+g7I48C8EH9tv7pJCw7GcHCEGNlGptzXpJ7YBB8p7MaMwEpA0tjr6nxr4zLngLddgiyrIzabhAi0R9w/ouNBcpa/ptr7p0ZLDoTp/9kSm2CTe80455clBdZmxamYHJnTfJzGCPvtSy1yEmUQPNU3LJD8q4aWFuRLwU7uJX4I/5dVBFAIcXgZ4cH2+SI9luQkIP4IbSu12rShQqEY1cidu6QtsDpk0qF23QIDAQAB";
	}
	
	/**
     * 签名方式
     * @return
     */
	public String getSignType() {
		return "RSA2";
	}
	
	/**
     * 字符编码格式
     * @return
     */
	public String getCharset() {
		return "utf-8";
	}
	
	/**
     * 支付宝网关
     * @return
     */
	public String getGatewayUrl() {
		return "https://openapi.alipaydev.com/gateway.do";
	}
	
	
}
