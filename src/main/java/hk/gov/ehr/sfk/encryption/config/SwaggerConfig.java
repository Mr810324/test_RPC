package hk.gov.ehr.sfk.encryption.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("SFK API Documentation")
                .description("API documentation for SFK")
                .version("1.0.0")
                .build();
    }
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                //用了select就必须有build，加上build后，回去发现select就剩两个方法,apis和paths
                //basePackage: 指定要扫描的包
                //any()全部扫描 ， none()都不扫描
                .apis(RequestHandlerSelectors.basePackage("hk.gov.ehr.sfk.encryption.controller") )
                .build();
    }

/*    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.test_rpc.controller.SfkController")) //这里写的是API接口所在的包位置
                .paths(PathSelectors.any())
                .build();
    }*/

/*    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("hk.health")
                .pathsToExclude("/apiSwitch/**")
                .build();
    }

    @Bean
    public OpenAPI publicOpenApi(@Autowired(required = false) ProjectInfo projectInfo) {
        String projectInfoStr = projectInfo == null ? "" : new GsonBuilder().setPrettyPrinting().create().toJson(projectInfo);
        return new OpenAPI()
                .info(new Info().title("CIMS-Interface-ECS-SVC")
                        .description("Checking the patient eligibility, e.g. government staff, permanent resident, medical waver etc \n " +
                                "Feature Highlights： \n" +
                                "1. CIMS2 needs to check patient eligibility by patient ID \n" +
                                "2. The action would be triggered by user through front-end web component (e.g. button), then call a REST API and fire a request to ECS system \n" +
                                "3. ECS system will return the checking result synchronously and display the front-end web application \n" +
                                "4. There are 3 types of ECS Systems which adopt the same mechanism to check different eligibility status of patient  \n" +
                                "\n" +
                                "\n" +
                                "gitVersionInfo: \n" + projectInfoStr
                        )
                        .version("v1.0")
                        .contact(new Contact().name("ECS")))
                .components(new Components().addSecuritySchemes(SecurityScheme.Type.APIKEY.toString(), apiKeySecuritySchema()))
                .security(Collections.singletonList(new SecurityRequirement().addList(SecurityScheme.Type.APIKEY.toString())));

    }

    private SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name("Authorization")
                .description("SAM3 JWT TOKEN")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);
    }*/

}