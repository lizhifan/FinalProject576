import java.io.*;

public class Wav {
	public int barcodeSize=1500;
	public WavFile wavFile;
	public Wav(String file) {
		try {
			wavFile=WavFile.openWavFile(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int[] barcode() {
		final int windowSize=(int)Math.ceil((double)wavFile.getNumFrames()/barcodeSize)*wavFile.getNumChannels();
		final int bufferSize=windowSize*wavFile.getNumChannels();
		final int scale=(int) (Long.MAX_VALUE >> (64 - wavFile.getValidBits()));
		double[] buffer=new double[bufferSize];
		int[] code=new int[barcodeSize];
		int framesRead;
		for(int i=0;i<barcodeSize;i++) {
			try {
				framesRead=wavFile.readFrames(buffer,windowSize);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (WavFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			double sum=0;
			for(int j=0;j<framesRead;j++) {
				buffer[j]=(buffer[j]+1)*(scale-1);
				if (buffer[j]<1)
					buffer[j]=1;
				buffer[j]=10*(17+Math.log10(buffer[j]));
				sum+=buffer[j];
				//System.out.println(Math.floor(buffer[j]));
			}
			sum/=framesRead*wavFile.getNumChannels();
			code[i]=(int) (((sum-170)/(10*(Math.log10(2*(scale-1))+17)-170))*255);
			System.out.println(sum+"\t"+code[i]);
		}
		
		return code;
	}
	
}
