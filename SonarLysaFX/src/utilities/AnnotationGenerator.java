package utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"id", "key", "name", "description", "qualifier", "language", "path"})
public class AnnotationGenerator
{

}
