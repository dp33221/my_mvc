package utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParameterMappingTokenHandler implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<>();


    public List<ParameterMapping> getParameterMappings() {
      return parameterMappings;
    }

    @Override
    public String handleToken(String content) {
      parameterMappings.add(buildParameterMapping(content));
      return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
      return new ParameterMapping(content);
    }


  }