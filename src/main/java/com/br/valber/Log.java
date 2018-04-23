package com.br.valber;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;

import com.br.valber.compact.Compactar;




public class Log {
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;


	public Log() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public void montarFileErro(Class arg1, String tipo, String msg)   {
		try {
			File dir = new File(new File("").getAbsolutePath() + "/logs");
			try {
				dir.mkdir();
			} catch (Exception e) {
			}
			
			BufferedWriter write = new BufferedWriter(
					new FileWriter(new File("").getAbsolutePath() + "/logs/erros.log", true));
			write.newLine();
			write.write("[" + arg1.getName() + "] - [" + LocalDate.now() + "] - "+tipo+" : " + msg);
			write.flush();
			write.close();

			File file = new File(new File("").getAbsolutePath() + "/logs");
			final long[] l = new long[] { getDirectorySize(file) };
			for (final long ll : l) {
				String tamanho = convertToStringRepresentation(ll);
				try {
					int intTamanho = Integer.parseInt(tamanho.substring(0, 3));
					if (tamanho.contains("KB") && intTamanho > 200) {
						for (File arq : file.listFiles()) {
							Compactar.compactarParaZip(arq.getAbsolutePath(), dir.getAbsolutePath()+"/"+System.getProperty("user.name")+"_"+LocalDate.now()+".zip");
							arq.delete();
						}
					}
				} catch (Exception e) {
				}

			}

		} catch (IOException e) {
			
		}
	}

	public static String convertToStringRepresentation(final long value) {
		final long[] dividers = new long[] { T, G, M, K, 1 };
		final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
		if (value < 1)
			throw new IllegalArgumentException("Invalid file size: " + value);
		String result = null;
		for (int i = 0; i < dividers.length; i++) {
			final long divider = dividers[i];
			if (value >= divider) {
				result = format(value, divider, units[i]);
				break;
			}
		}
		return result;
	}

	private static String format(final long value, final long divider, final String unit) {
		final double result = divider > 1 ? (double) value / (double) divider : (double) value;
		return new DecimalFormat("#,##0.#").format(result) + " " + unit;
	}

	public static long getDirectorySize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				length += file.length();
			} else {
				length += getDirectorySize(file);
			}
		}
		return length;
	}
	
	

	

	
}
