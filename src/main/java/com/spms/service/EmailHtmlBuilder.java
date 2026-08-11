package com.spms.service;

/**
 * HTML email templates (same style as MedNexus EmailHtmlBuilder).
 * Keep this focused on OTP emails for now.
 */
final class EmailHtmlBuilder {

    private static final String BRAND = "MedNexus";
    private static final String TEAL = "#0f766e";
    private static final String TEAL_LIGHT = "#f0fdfa";
    private static final String SLATE = "#334155";
    private static final String MUTED = "#64748b";

    private EmailHtmlBuilder() {
    }

    static String otpVerification(
            String headline,
            String intro,
            String code,
            int ttlMinutes,
            String frontendUrl) {
        String safeHeadline = escape(headline);
        String safeIntro = escape(intro);
        String safeCode = escape(code);

        return layout(
                """
                <h1 style="margin:0 0 12px;font-size:22px;line-height:1.3;color:#0f172a;font-weight:700;">%s</h1>
                <p style="margin:0 0 24px;font-size:15px;line-height:1.7;color:%s;">%s</p>
                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="margin:0 0 24px;">
                  <tr>
                    <td align="center" style="padding:24px 16px;border-radius:12px;background:%s;border:1px solid #99f6e4;">
                      <p style="margin:0 0 8px;font-size:12px;font-weight:600;letter-spacing:0.08em;text-transform:uppercase;color:%s;">Verification code</p>
                      <p style="margin:0;font-size:34px;line-height:1;font-weight:700;letter-spacing:0.35em;color:%s;font-family:Consolas,'Courier New',monospace;">%s</p>
                    </td>
                  </tr>
                </table>
                <p style="margin:0 0 12px;font-size:14px;line-height:1.6;color:%s;">
                  This code expires in <strong>%d minutes</strong>. Enter it on the %s website to continue.
                </p>
                <p style="margin:0;font-size:14px;line-height:1.6;color:%s;">
                  For your security, never share this code with anyone. %s will never ask for it by phone or message.
                </p>
                """
                        .formatted(
                                safeHeadline,
                                SLATE,
                                safeIntro,
                                TEAL_LIGHT,
                                TEAL,
                                TEAL,
                                safeCode,
                                MUTED,
                                ttlMinutes,
                                BRAND,
                                MUTED,
                                BRAND),
                headline,
                frontendUrl);
    }

    private static String brandHeader() {
        return """
                <p style="margin:0;font-size:30px;line-height:1.1;font-weight:700;letter-spacing:-0.03em;color:%s;">%s</p>
                <p style="margin:8px 0 0;font-size:13px;line-height:1.4;font-weight:500;letter-spacing:0.04em;text-transform:uppercase;color:%s;">Pharmacy Management</p>
                """
                .formatted(TEAL, BRAND, MUTED);
    }

    private static String layout(String bodyContent, String preheader, String frontendUrl) {
        String safePreheader = escape(preheader);
        String safeWebsiteUrl = escape(
                frontendUrl == null || frontendUrl.isBlank()
                        ? "http://localhost:4200"
                        : frontendUrl.replaceAll("/$", ""));

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                  <title>%s</title>
                </head>
                <body style="margin:0;padding:0;background-color:#eef2f7;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Helvetica Neue',Arial,sans-serif;">
                  <div style="display:none;max-height:0;overflow:hidden;opacity:0;">%s</div>
                  <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color:#eef2f7;padding:32px 16px;">
                    <tr>
                      <td align="center">
                        <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="max-width:560px;">
                          <tr>
                            <td align="center" style="padding-bottom:24px;">
                              %s
                            </td>
                          </tr>
                          <tr>
                            <td style="background-color:#ffffff;border-radius:16px;border:1px solid #e2e8f0;box-shadow:0 8px 24px rgba(15,23,42,0.06);padding:36px 32px;">
                              %s
                            </td>
                          </tr>
                          <tr>
                            <td align="center" style="padding:24px 12px 8px;">
                              <a href="%s" style="font-size:14px;font-weight:600;color:%s;text-decoration:underline;">Visit our website</a>
                            </td>
                          </tr>
                          <tr>
                            <td align="center" style="padding:8px 12px 0;">
                              <p style="margin:0 0 8px;font-size:12px;line-height:1.5;color:%s;">&copy; %s. All rights reserved.</p>
                              <p style="margin:0;font-size:12px;line-height:1.5;color:#94a3b8;">This is an automated message. Please do not reply to this email.</p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """
                .formatted(BRAND, safePreheader, brandHeader(), bodyContent, safeWebsiteUrl, TEAL, MUTED, BRAND);
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
