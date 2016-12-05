using SQLite;

namespace currency_converter.model
{
    public class Currency
    {
        [PrimaryKey, AutoIncrement]
        public int id { get; set; }
        public string code { get; set; }
        public string name { get; set; }
        public float toCurrency { get; set; }
        public Currency(string code, string name)
        {
            this.code = code;
            this.name = name;
            toCurrency = 1;
        }
    }
}