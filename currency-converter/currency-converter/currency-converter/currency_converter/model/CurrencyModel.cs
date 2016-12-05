using SQLite;

namespace currency_converter.model
{
    public class CurrencyModel
    {
        [PrimaryKey, AutoIncrement]
        public int id { get; set; }
        public string code { get; set; }
        public string name { get; set; }
        public float toCurrency { get; set; }
    }
}