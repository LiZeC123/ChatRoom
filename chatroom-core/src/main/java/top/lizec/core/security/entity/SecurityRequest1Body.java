package top.lizec.core.security.entity;

public class SecurityRequest1Body {
    private String algorithm;
    private Long randomNum;

    public SecurityRequest1Body() {
    }

    public SecurityRequest1Body(String algorithm, Long randomNum) {
        this.algorithm = algorithm;
        this.randomNum = randomNum;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Long getRandomNum() {
        return randomNum;
    }
}
