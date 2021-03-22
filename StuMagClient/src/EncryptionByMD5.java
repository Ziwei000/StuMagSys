
public class EncryptionByMD5 {
	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };// 用来将字节转换成16进制表示的字符
		try {
			//java.security.MessageDigest类用于为应用程序提供信息摘要算法的功能
			//信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值。
			//MessageDigest 通过其getInstance系列静态函数来进行实例化和初始化。
			//MessageDigest 对象通过使用 update 方法处理数据。任何时候都可以调用 reset 方法重置摘要。
			//一旦所有需要更新的数据都已经被更新了，应该调用 digest 方法之一完成哈希计算并返回结果。
			//对于给定数量的更新数据，digest 方法只能被调用一次。digest 方法被调用后，MessageDigest  对象被重新设置成其初始状态。
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();// MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
			// 进制需要 32 个字符
			int k = 0;// 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
				// 进制字符的转换
				byte byte0 = tmp[i];// 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换,// >>>
				// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换

			}
			s = new String(str);// 换后的结果转换为字符串

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	public static void main(String[] args){
	
		String test=EncryptionByMD5.getMD5("111111".getBytes());
		System.out.println(test);

	}
}
