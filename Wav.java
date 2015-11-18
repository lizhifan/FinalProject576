import java.io.*;

public class Wav {
	public int barcodeSize=5000;
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
				sum+=Math.abs(buffer[j]);
				//System.out.println(Math.abs(buffer[j]));
			}
			sum/=framesRead*wavFile.getNumChannels();
			code[i]=(int)(sum*255);
			System.out.println(code[i]);
		}
		return code;
	}
	
}
