using SQLite;

namespace currency_converter.model
{
    public class CurrencyModel
    {
        [PrimaryKey, NotNull]
        public string code { get; set; }
        [NotNull]
        public string name { get; set; }
        [NotNull]
        public double toCurrency { get; set; }
        [NotNull]
        public bool isFavourite { get; set; }

        public CurrencyModel() { }
        
        public CurrencyModel(string code, string name)
        {
            this.code = code;
            this.name = name;
            toCurrency = 1;
            isFavourite = false;
        }

        public CurrencyModel(string code, string name, bool isFavourite)
        {
            this.code = code;
            this.name = name;
            toCurrency = 1;
            this.isFavourite = isFavourite;
        }

        public CurrencyModel(string code, string name, double toCurrency, bool isFavourite)
        {
            this.code = code;
            this.name = name;
            this.toCurrency = toCurrency;
            this.isFavourite = isFavourite;
        }
    }
}