package com.sceneclipper.officeIntros;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;

public class OfficeIntros {
	public static void main(String[] args) throws IOException, InterruptedException {
		String officeTheme = "theoffice.wav";
		String outputFolder = "Intros";
		
		ArrayList<String> intros = new ArrayList<String>();
				
		File introDir = new File("/home/tom/Development/theoffice/" + outputFolder);
		if(!introDir.exists()) {
			introDir.mkdir();
		}

		//TODO whole directory at a time
		Files.walk(Paths.get("/home/tom/Development/theoffice")).forEach(filePath -> {
			
			if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".mkv")) {
				intros.add(filePath.toString());
				
				//String officeEpisode = "/home/tom/Development/theoffice/[5.26]_Company_Picnic.avi";
				String officeEpisode = filePath.toString();
				
				String officeEpisodeSound = officeEpisode.substring(0, officeEpisode.lastIndexOf(".")) + "-sound.wav";
				
				String transcodeCommand = "avconv -i " + officeEpisode + " -ss 0 -t 500 -acodec pcm_s16le -ac 1 -ar 16000 " + officeEpisodeSound;
				System.out.println(transcodeCommand);
				
				ProcessBuilder pb = new ProcessBuilder(Arrays.asList(transcodeCommand.split(" ")));
				pb.redirectErrorStream(true);
				Process p = null;
				try {
					p = pb.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); 

				String line = null; 

				try {
					while((line=br.readLine())!=null){ 
						System.out.println("---------"+line); 
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
				Wave episodeWave = new Wave(officeEpisodeSound);
				Wave themeWave = new Wave(officeTheme);

				FingerprintManager fpm = new FingerprintManager();
				byte themeFingerprint[] = fpm.extractFingerprint(themeWave);
				byte episodeFingerprint[] = fpm.extractFingerprint(episodeWave);
				
				FingerprintSimilarityComputer compareFingerprints = new FingerprintSimilarityComputer(episodeFingerprint, themeFingerprint);
				FingerprintSimilarity similarity = compareFingerprints.getFingerprintsSimilarity();
				float timePosition = similarity.getsetMostSimilarTimePosition();
				
				System.out.println("Match found at " + timePosition);
				
				String endTime = String.valueOf(timePosition);
						
				transcodeCommand = "avconv -i " + officeEpisode + " -c:v libx264 -s hd1080 -c:a ac3 -ss 0 -t " + endTime + " "
						+ officeEpisode.substring(0, officeEpisode.lastIndexOf(File.separator)) 
						+ File.separator + outputFolder + File.separator
						+ officeEpisode.substring(officeEpisode.lastIndexOf(File.separator), officeEpisode.lastIndexOf(".")) + "-intro"
						+ officeEpisode.substring(officeEpisode.lastIndexOf("."), officeEpisode.length());

				System.out.println(transcodeCommand);
				
				pb = new ProcessBuilder(Arrays.asList(transcodeCommand.split(" ")));
				pb.redirectErrorStream(true);
				try {
					p = pb.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				br = new BufferedReader(new InputStreamReader(p.getInputStream())); 

				line = null; 

				try {
					while((line=br.readLine())!=null){ 
						System.out.println("---------"+line); 
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					p = new ProcessBuilder("rm", officeEpisodeSound).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
			
		});
		
		String concat = "\"";
		for(int i = 0; i < intros.size(); i++) {
			concat += intros.get(i) + "|";
		}
		
		concat = concat.substring(0, concat.lastIndexOf("|"));
		concat += "\"";
		
		String joinIntros = "avconv -i concat:" + concat + " -c:v libx264 -c:a ac3 Intros.avi";
		
		System.out.println(joinIntros);
		
		ProcessBuilder pb = new ProcessBuilder(Arrays.asList(joinIntros.split(" ")));
		pb.redirectErrorStream(true);
		Process p = pb.start();
			
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); 

		String line = null; 

		while((line=br.readLine())!=null){ 
			System.out.println("---------"+line); 
		}
		
	}
}
