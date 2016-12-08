using currency_converter.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace currency_converter.utils
{
    public static class Utils
    {
        public static CurrencyModel GetEuroCurrency(List<CurrencyModel> currencies)
        {
            foreach (CurrencyModel c in currencies)
            {
                if (c.code == "EUR")
                    return c;
            }

            return null;
        }

        public static CurrencyModel GetMainCurrency(List<CurrencyModel> currencies)
        {
            foreach (CurrencyModel c in currencies)
            {
                if (c.isFavourite)
                    return c;
            }

            return null;
        }

        public static int GetMainCurrencyIndex(List<CurrencyModel> currencies)
        {
            for(int i = 0; i < currencies.Count; i++)
            {
                if (currencies[i].isFavourite)
                    return i;
            }

            return -1;
        }

        public static List<CurrencyModel> ConvertListTo(CurrencyModel euroC, CurrencyModel currentSC, List<CurrencyModel> list)
        {
            List<CurrencyModel> currencies = new List<CurrencyModel>();

            foreach(CurrencyModel c in list)
            {
                if (currentSC.code == c.code)
                    currencies.Add(new CurrencyModel(c.code, c.name, 1, c.isFavourite));

                else if (c.code == "EUR")
                    currencies.Add(new CurrencyModel(c.code, c.name, euroC.toCurrency / currentSC.toCurrency, c.isFavourite));

                else
                {
                    double euro_selected_ratio = euroC.toCurrency / currentSC.toCurrency;

                    currencies.Add(new CurrencyModel(c.code, c.name, euro_selected_ratio * c.toCurrency, c.isFavourite));
                }
            }

            return currencies;
        }

        public static double ConvertValue(CurrencyModel euroC, CurrencyModel mainC, CurrencyModel selectedC)
        {
            double euro_main_ratio = euroC.toCurrency / mainC.toCurrency;

            return euro_main_ratio * selectedC.toCurrency;
        }
    }
}
