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

        public CurrencyModel() { }
        
        public CurrencyModel(string code, string name)
        {
            this.code = code;
            this.name = name;
            this.toCurrency = 1;
        }

        public CurrencyModel(string code, string name, double toCurrency)
        {
            this.code = code;
            this.name = name;
            this.toCurrency = toCurrency;
        }
    }
}