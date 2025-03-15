package guru.qa.niffler.data.entity.user;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CurrencyValues {
    RUB("₽"), USD("$"), EUR("€"), KZT("₸");
    public final String sign;

}
