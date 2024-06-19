package com.todoc.web;

import org.springframework.stereotype.Service;

@Service("template")
public class EmailTemplate 
{
    private String verificationCode;

    public void setVerificationCode(String verificationCode) 
    {
        this.verificationCode = verificationCode;
    }
	
	public String idFindTemplate() throws Exception
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<!DOCTYPE HTML PUBLIC '-//W3C//DTD XHTML 1.0 Transitional //EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n"
				+ "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:v='urn:schemas-microsoft-com:vml' xmlns:o='urn:schemas-microsoft-com:office:office'>\n"
				+ "<head>\n"
				+ "  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n"
				+ "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n"
				+ "  <meta name='x-apple-disable-message-reformatting'>\n"
				+ "  <meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
				+ "  <title></title>\n"
				+ "<link rel='stylesheet' href='css/mailTemplate.css' type='text/css'>\n"
				+ "<link href='https://fonts.googleapis.com/css?family=Raleway:400,700' rel='stylesheet' type='text/css'>\n"
				+ "</head>\n"
				+ "<body class='clean-body u_body' style='margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #f9f9ff;color: #000000'>\n"
				+ "  <table style='border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9ff;width:100%' cellpadding='0' cellspacing='0'>\n"
				+ "  <tbody>\n"
				+ "  <tr style='vertical-align: top'>\n"
				+ "    <td style='word-break: break-word;border-collapse: collapse !important;vertical-align: top'>\n"
				+ "<div class='u-row-container' style='padding: 0px;background-color: transparent'>\n"
				+ "  <div class='u-row' style='margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;'>\n"
				+ "    <div style='border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;'>\n"
				+ "<div class='u-col u-col-100' style='max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;'>\n"
				+ "  <div style='background-color: #ffffff;height: 100%;width: 100% !important;'>\n"
				+ "  <div style='box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;'>\n"
				+ "<table id='u_content_heading_1' style=' sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'>\n"
				+ "  <tbody><tr><td class='v-container-padding-padding' style='overflow-wrap:break-word;word-break:break-word;padding:10px 10px 30px; sans-serif;' align='left'>\n"
				+ "    <h1 class='v-font-size' style='margin: 0px; line-height: 140%; text-align: center; word-wrap: break-word; font-size: 28px; font-weight: 400;'><span><strong>[todoc] 이메일 인증</strong></span></h1>\n"
				+ "      </td></tr></tbody></table></div></div></div></div></div></div>\n"
				+ "<div class='u-row-container' style='padding: 0px;background-color: transparent'>\n"
				+ "  <div class='u-row' style='margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;'>\n"
				+ "    <div style='border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;'>\n"
				+ "      <div class='u-col u-col-100' style='max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;'>\n"
				+ "  <div style='background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;'>\n"
				+ "  <div style='box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;'>\n"
				+ "    <table width='100%' cellpadding='0' cellspacing='0' border='0'>\n"
		
				+ "      <tr>");
		
				
				sb.append( "        </tr></table>  \n"
				+ "<table id='u_content_heading_2' style=' sans-serif; text-align:center' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0' >\n"
				+ "  <tbody><tr><td class='v-container-padding-padding' style='overflow-wrap:break-word;word-break:break-word;padding:40px 60px 10px; sans-serif;' align='center'>\n"
				+ "    <h1 class='v-font-size' style='margin: 0px; line-height: 140%; text-align: left; word-wrap: break-word; font-size: 16px; font-weight: 400;'>비밀번호를 분실했거나 비밀번호를 재설정하려면, <br>아래 인증번호를 사용하여 인증해주세요.</h1>\n"
				+ "      </td></tr></tbody></table>\n"
				+ "<table id='u_content_text_2' style=' sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'>\n"
				+ "  <tbody><tr><td class='v-container-padding-padding' style='overflow-wrap:break-word;word-break:break-word;padding:10px 60px; sans-serif;' align='left'>\n"
				+ "  <div class='v-font-size' style='font-size: 14px; color: #1386e5; line-height: 140%; text-align: left; word-wrap: break-word;'>\n");
				
				sb.append("<p style='font-size: 14px; line-height: 170%;'>인증번호: ").append(verificationCode).append("</p>");
			    
				sb.append("\n"
				+ ""
				+ "  </div></td></tr></tbody></table>  <br><br></div></div></div></div></div></div>\n"
				+ "<div class='u-row-container' style='padding: 0px;background-color: transparent'>\n"
				+ "  <div class='u-row' style='margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;'>\n"
				+ "    <div style='border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;'>\n"
				+ "<div class='u-col u-col-100' style='max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;'>\n"
				+ "  <div style='height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;'>\n"
				+ "  <div style='box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;'>\n"
				+ "<table id='u_content_text_deprecated_1' style=' sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'>\n"
				+ "  <tbody><tr><td class='v-container-padding-padding' style='overflow-wrap:break-word;word-break:break-word;padding:10px 100px 30px; sans-serif;' align='left'><br><br>\n"
				+ "  <div class='v-font-size' style='font-size: 14px; line-height: 170%; text-align: center; word-wrap: break-word;'>\n"
				+ "    <p style='font-size: 14px; line-height: 170%;'>UNSUBSCRIBE   |   PRIVACY POLICY   |   WEB</p>\n"
				+ "<p style='font-size: 14px; line-height: 170%;'> </p>\n"
				+ "<p style='font-size: 14px; line-height: 170%;'>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore.</p>\n"
				+ "  </div></td></tr></tbody></table>\n"
				+ "<table style=' sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'>\n"
				+ "  <tbody><tr><td class='v-container-padding-padding' style='overflow-wrap:break-word;word-break:break-word;padding:0px; sans-serif;' align='left'><br><br> \n"
				+ "  <table height='0px' align='center' border='0' cellpadding='0' cellspacing='0' width='100%' style='border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%'>\n"
				+ "    <tbody><tr style='vertical-align: top'><td style='word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%'>\n"
				+ "          <span>&#160;</span></td></tr></tbody></table></td></tr></tbody></table>\n"
				+ "<table id='u_content_image_2' style=' sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'>\n"
				+ "  <tbody><tr><td class='v-container-padding-padding' style='overflow-wrap:break-word;word-break:break-word;padding:30px 10px 40px; sans-serif;' align='left'></td></tr></tbody>\n"
				+ "</table></div></div></div></div></div></div></td></tr></tbody></table>\n"
				+ "</body>\n"
				+ "</html>");
		
		return sb.toString();
	}
}
