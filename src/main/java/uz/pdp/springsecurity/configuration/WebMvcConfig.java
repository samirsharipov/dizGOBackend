package uz.pdp.springsecurity.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(false);
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/")
//                .resourceChain(false)
//                .addResolver(new PushStateResourceResolver());
//    }
//
//    private class PushStateResourceResolver implements ResourceResolver {
//        private Resource index = new ClassPathResource("/static/index.html");
//        private List<String> handledExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "otf", "woff", "appcache", "jpg", "jpeg", "gif", "ico");
//        private List<String> ignoredPaths = Arrays.asList("api");
//
//        @Override
//        public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
//            return resolve(requestPath, locations);
//        }
//
//        @Override
//        public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
//            Resource resolvedResource = resolve(resourcePath, locations);
//            if (resolvedResource == null) {
//                return null;
//            }
//            try {
//                return resolvedResource.getURL().toString();
//            } catch (IOException e) {
//                return resolvedResource.getFilename();
//            }
//        }
//
//        private Resource resolve(String requestPath, List<? extends Resource> locations) {
//            if (isIgnored(requestPath)) {
//                return null;
//            }
//            if (isHandled(requestPath)) {
//                return locations.stream()
//                        .map(loc -> createRelative(loc, requestPath))
//                        .filter(resource -> resource != null && resource.exists())
//                        .findFirst()
//                        .orElseGet(null);
//            }
//            return index;
//        }
//
//        private Resource createRelative(Resource resource, String relativePath) {
//            try {
//                return resource.createRelative(relativePath);
//            } catch (IOException e) {
//                return null;
//            }
//        }
//
//        private boolean isIgnored(String path) {
//            return ignoredPaths.contains(path);
//        }
//
//        private boolean isHandled(String path) {
//            String extension = StringUtils.getFilenameExtension(path);
//            return handledExtensions.stream().anyMatch(ext -> ext.equals(extension));
//        }
//    }

}
