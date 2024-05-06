package by.bondarev.moviesearch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Movies Info Api",
                description = "A movie information storage system synchronized with the Kinopoisk API, imdb API and other platforms", version = "2.0.3",
                contact = @Contact(
                        name = "Bondarev Kirill",
                        email = "kirillbond2017@gmail.com",
                        url = "https://t.me/wupdp"
                )
        )
)
public class SwaggerConfig {

}
