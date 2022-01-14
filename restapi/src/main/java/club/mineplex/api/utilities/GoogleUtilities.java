package club.mineplex.api.utilities;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@UtilityClass
@Log4j2
public class GoogleUtilities {
    private static final String APPLICATION_NAME = "MineplexService";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Sheets sheetsService = null;

    /**
     * Creates an authorized Credential object.
     *
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @SuppressWarnings("deprecation")
    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        final InputStream in = GoogleUtilities.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        // TODO: Do a proper implementation
        return GoogleCredential.fromStream(in).createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    }

    public Sheets getSheetsService() {
        if (sheetsService == null) {
            try {
                final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                final Credential credential = GoogleUtilities.getCredentials(httpTransport);

                sheetsService = new Sheets.Builder(
                        httpTransport,
                        JSON_FACTORY,
                        credential
                ).setApplicationName(APPLICATION_NAME).build();
            } catch (final IOException | GeneralSecurityException e) {
                log.catching(e);
            }
        }

        return sheetsService;
    }
}