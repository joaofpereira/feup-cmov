﻿using SQLite;

namespace currency_converter.model
{
    public class CurrencyModel
    {
        [PrimaryKey, AutoIncrement]
        public long id { get; set; }
        [NotNull]
        public string code { get; set; }
        [NotNull]
        public string name { get; set; }
        [NotNull]
        public float toCurrency { get; set; }
       
    }
}