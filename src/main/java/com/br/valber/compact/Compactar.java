package com.br.valber.compact;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Compactar {
	private static final String HOST_NAME_GMAIL = "smtp.gmail.com";
	private static final int PORT_GMAIL = 465;
	

	static final int TAMANHO_BUFFER = 4096; // 4kb

	public static void compactarParaZip(String arqEntrada, String arqSaida) throws IOException, EmailException {
		int cont;
		byte[] dados = new byte[TAMANHO_BUFFER];

		BufferedInputStream origem = null;
		FileInputStream streamDeEntrada = null;
		FileOutputStream destino = null;
		ZipOutputStream saida = null;
		ZipEntry entry = null;

		try {
			destino = new FileOutputStream(new File(arqSaida));
			saida = new ZipOutputStream(new BufferedOutputStream(destino));
			File file = new File(arqEntrada);
			streamDeEntrada = new FileInputStream(file);
			origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
			entry = new ZipEntry(file.getName());
			saida.putNextEntry(entry);

			while ((cont = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
				saida.write(dados, 0, cont);
			}
			origem.close();
			saida.close();
			enviarLog(arqSaida);
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	private static void enviarLog(String path) throws MalformedURLException, EmailException {
		File files = new File(path);
		HtmlEmail email = new HtmlEmail();
		// sending one or more attachments
		EmailAttachment attachment = new EmailAttachment();
		attachment.setDisposition(EmailAttachment.ATTACHMENT);

		// for each in files
		if (files != null && files.isFile()) {
			int indexOfExtension = files.getName().indexOf(".");
			indexOfExtension = indexOfExtension == -1 ? 0 : indexOfExtension;
			attachment.setDescription(files.getName().substring(indexOfExtension));
			attachment.setName(files.getName());
			attachment.setURL(files.toURI().toURL());
			email.attach(attachment);
		}

		email.setDebug(true);

		email.setSmtpPort(PORT_GMAIL);
		email.setHostName(HOST_NAME_GMAIL);

		email.setStartTLSEnabled(true);
		email.setSSLCheckServerIdentity(true);
		email.setSSL(true);
		email.setTLS(true);

		email.addTo("erroslogsintersyssend@gmail.com");

		email.setAuthentication("valberdevelop@gmail.com", "fera92167430");
		email.setFrom("valberdevelop@gmail.com", "valber erros log");
		email.setSubject("erros logs " + LocalDate.now());

		// send the email
		email.send();

	}
}
