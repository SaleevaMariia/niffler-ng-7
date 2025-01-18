package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String spendJdbcUrl();

  String currencyJdbcUrl();

  String ghUrl();

  String authUrl();

  String authJdbcUrt();

  String gatewayUrl();

  String userdataUrl();

  String userdataJdbcUrl();

}
