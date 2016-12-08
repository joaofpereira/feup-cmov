using currency_converter.model;
using currency_converter.utils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;

namespace currency_converter
{
    public partial class WalletPage : ContentPage
    {
        DataAccess db;
        List<WalletModel> wallet;
        List<CurrencyModel> currencies;
        List<Color> colors;

        CurrencyModel euroC;
        CurrencyModel mainC;
        CurrencyModel selectedC;
        int mainCurrencyIndex;
        double walletValue;

        Label header;
        StackLayout mainLayout;
        StackLayout chart;
        Picker currencyPicker;

        public WalletPage()
        {
            db = DataAccess.GetDB;

            wallet = db.GetWalletEntries();

            currencies = db.GetAllCurrency();
            euroC = Utils.GetEuroCurrency(currencies);
            mainC = Utils.GetMainCurrency(currencies);
            mainCurrencyIndex = Utils.GetMainCurrencyIndex(currencies);
            selectedC = currencies[mainCurrencyIndex];

            colors = CreateColorsList();

            // Calculate Total Wallet Value

            UpdateWalletValue();

            Title = "Wallet";

            mainLayout = new StackLayout() { };

            header = new Label
            {
                Text = "Wallet Value: " + string.Format("{0:0.00}", walletValue) + " " + mainC.code,
                Font = Font.BoldSystemFontOfSize(20),
                HorizontalOptions = LayoutOptions.Center
            };

            currencyPicker = new Picker();
            Device.OnPlatform(
                Android: () => currencyPicker = new Picker()
                {
                    Title = "Currency",
                    WidthRequest = 100,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                },
                WinPhone: () => currencyPicker = new Picker()
                {
                    Title = "Currency",
                    WidthRequest = 100,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center
                }
            );

            foreach (CurrencyModel c in currencies)
            {
                currencyPicker.Items.Add(c.code);
            }

            currencyPicker.SelectedIndex = mainCurrencyIndex;
            currencyPicker.SelectedIndexChanged += OnCurrencyPickerChanged;

            UpdateChart();

            ToolbarItem addRemoveToolbarItem = new ToolbarItem();
            Device.OnPlatform(
                Android: () => addRemoveToolbarItem = new ToolbarItem()
                {
                    Text = "Add/Remove Currency",
                    Order = ToolbarItemOrder.Secondary
                },
                WinPhone: () => addRemoveToolbarItem = new ToolbarItem()
                {
                    Text = "Add/Remove Currency",
                    Order = ToolbarItemOrder.Secondary
                }
            );
            addRemoveToolbarItem.Clicked += OnClickedAddRemoveToolbarItem;

            ToolbarItem ratesToolbarItem = new ToolbarItem();
            Device.OnPlatform(
                Android: () => ratesToolbarItem = new ToolbarItem()
                {
                    Text = "Currency Rates",
                    Order = ToolbarItemOrder.Primary
                },
                WinPhone: () => ratesToolbarItem = new ToolbarItem()
                {
                    Text = "Currency Rates",
                    Order = ToolbarItemOrder.Secondary
                }
            );
            ratesToolbarItem.Clicked += OnClickedRatesToolbarItem;

            ToolbarItems.Add(addRemoveToolbarItem);
            ToolbarItems.Add(ratesToolbarItem);

            mainLayout.Children.Add(currencyPicker);
            mainLayout.Children.Add(header);
            mainLayout.Children.Add(chart);

            Content = mainLayout;
        }

        private void UpdateChart()
        {
            chart = new StackLayout() {};

            chart.Orientation = StackOrientation.Vertical;
            chart.VerticalOptions = LayoutOptions.CenterAndExpand;
            chart.HorizontalOptions = LayoutOptions.CenterAndExpand;

            StackLayout bars = new StackLayout() {};
            bars.Orientation = StackOrientation.Horizontal;

            bars.HeightRequest = 250;

            for(int i = 0; i < wallet.Count; i++)
            {
                CurrencyModel walletCurrency = GetWalletCurrency(wallet[i]);

                Debug.WriteLine("Percentagem de " + walletCurrency.code + ": " + Math.Round(250 * ((wallet[i].amount / Utils.ConvertValue(euroC, mainC, walletCurrency)) / walletValue)));

                BoxView box = new BoxView();
                Device.OnPlatform(
                    Android: () => box = new BoxView()
                    {
                        Color = colors[i],
                        WidthRequest = 260 / wallet.Count,
                        HeightRequest = Math.Round(250 * ((wallet[i].amount / Utils.ConvertValue(euroC, mainC, walletCurrency)) / walletValue)),
                        VerticalOptions = LayoutOptions.End
                    },
                    WinPhone: () => box = new BoxView()
                    {
                        Color = colors[i],
                        WidthRequest = 600 / wallet.Count,
                        HeightRequest = Math.Round(250 * ((wallet[i].amount / Utils.ConvertValue(euroC, mainC, walletCurrency)) / walletValue)),
                        VerticalOptions = LayoutOptions.End
                    }
                );

                bars.Children.Add(box);
            }

            StackLayout walletAmounts = new StackLayout() { };
            walletAmounts.Orientation = StackOrientation.Horizontal;

            for (int i = 0; i < wallet.Count; i++)
            {
                Label amountLabel = new Label();
                Device.OnPlatform(
                    Android: () => amountLabel = new Label()
                    {
                        Text = "" + wallet[i].amount,
                        WidthRequest = 260 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center,
                        Font = Font.SystemFontOfSize(NamedSize.Micro),
                    },
                    WinPhone: () => amountLabel = new Label()
                    {
                        Text = "" + wallet[i].amount,
                        WidthRequest = 600 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center
                    }
                );

                walletAmounts.Children.Add(amountLabel);
            }

            StackLayout barsLabels = new StackLayout() {};
            barsLabels.Orientation = StackOrientation.Horizontal;

            for (int i = 0; i < wallet.Count; i++)
            {
                Font barLabelFont;

                if (wallet.Count == 11)
                    barLabelFont = Font.SystemFontOfSize(NamedSize.Micro);
                else
                    barLabelFont = Font.SystemFontOfSize(NamedSize.Small);

                Label boxLabel = new Label();
                Device.OnPlatform(
                    Android: () => boxLabel = new Label()
                    {
                        Text = wallet[i].code,
                        WidthRequest = 260 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center,
                        Font = barLabelFont,
                    },
                    WinPhone: () => boxLabel = new Label()
                    {
                        Text = wallet[i].code,
                        WidthRequest = 600 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center
                    }
                );

                barsLabels.Children.Add(boxLabel);
            }

            BoxView divider = new BoxView();
            Device.OnPlatform(
                Android: () => {
                    divider = new BoxView() { Color = Color.FromHex("#3498DB"), WidthRequest = 260, HeightRequest = 2 };
                },
                WinPhone: () =>
                {
                    divider = new BoxView() { Color = Color.Black, WidthRequest = 600, HeightRequest = 2 };
                }
            );

            StackLayout convertedLabels = new StackLayout() { };
            convertedLabels.Orientation = StackOrientation.Horizontal;

            for (int i = 0; i < wallet.Count; i++)
            {
                CurrencyModel walletCurrency = GetWalletCurrency(wallet[i]);

                Label convertedLabel = new Label();
                Device.OnPlatform(
                    Android: () => convertedLabel = new Label()
                    {
                        Text = string.Format("{0:0.00}", wallet[i].amount / Utils.ConvertValue(euroC, mainC, walletCurrency)),
                        WidthRequest = 260 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center,
                        Font = Font.SystemFontOfSize(NamedSize.Micro),
                    },
                    WinPhone: () => convertedLabel = new Label()
                    {
                        Text = string.Format("{0:0.00}", wallet[i].amount / Utils.ConvertValue(euroC, mainC, walletCurrency)),
                        WidthRequest = 600 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center
                    }
                );

                convertedLabels.Children.Add(convertedLabel);
            }

            StackLayout selectedCurrencyLabels = new StackLayout() { };
            selectedCurrencyLabels.Orientation = StackOrientation.Horizontal;

            for (int i = 0; i < wallet.Count; i++)
            {
                Font selectedLabelFont;

                if (wallet.Count == 11)
                    selectedLabelFont = Font.SystemFontOfSize(NamedSize.Micro);
                else
                    selectedLabelFont = Font.SystemFontOfSize(NamedSize.Small);

                Label selectedLabel = new Label();
                Device.OnPlatform(
                    Android: () => selectedLabel = new Label()
                    {
                        Text = currencies[currencyPicker.SelectedIndex].code,
                        WidthRequest = 260 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center,
                        Font = selectedLabelFont,
                    },
                    WinPhone: () => selectedLabel = new Label()
                    {
                        Text = currencies[currencyPicker.SelectedIndex].code,
                        WidthRequest = 600 / wallet.Count,
                        HeightRequest = 20,
                        HorizontalTextAlignment = TextAlignment.Center
                    }
                );

                selectedCurrencyLabels.Children.Add(selectedLabel);
            }

            chart.Children.Add(bars);
            chart.Children.Add(walletAmounts);
            chart.Children.Add(barsLabels);
            chart.Children.Add(divider);
            chart.Children.Add(convertedLabels);
            chart.Children.Add(selectedCurrencyLabels);

        }

        private void UpdateWalletValue()
        {
            walletValue = 0;

            foreach (WalletModel w in wallet)
                walletValue += w.amount / Utils.ConvertValue(euroC, mainC, GetWalletCurrency(w));
        }

        private CurrencyModel GetWalletCurrency(WalletModel walletCurrency)
        {
            foreach (CurrencyModel c in currencies)
                if (c.code == walletCurrency.code)
                    return c;

            return null;
        }

        async void OnClickedAddRemoveToolbarItem(object sender, EventArgs e)
        {
            AddRemoveCurrencyPage newPage = new AddRemoveCurrencyPage();
            await Navigation.PushAsync(newPage, true);
        }

        async void OnClickedRatesToolbarItem(object sender, EventArgs e)
        {
            RatesPage newPage = new RatesPage();
            await Navigation.PushAsync(newPage, true);
        }

        private void OnCurrencyPickerChanged(object sender, EventArgs e)
        {
            mainC = currencies[currencyPicker.SelectedIndex];
            UpdateWalletValue();

            mainLayout.Children.Remove(header);
            mainLayout.Children.Remove(chart);

            header.Text = "Wallet Value: " + string.Format("{0:0.00}", walletValue) + " " + mainC.code;
            UpdateChart();

            mainLayout.Children.Add(header);
            mainLayout.Children.Add(chart);
        }

        private List<Color> CreateColorsList()
        {
            List<Color> colors = new List<Color>();

            colors.Add(Color.Blue);
            colors.Add(Color.Red);
            colors.Add(Color.Green);
            colors.Add(Color.Yellow);
            colors.Add(Color.Silver);
            colors.Add(Color.Black);
            colors.Add(Color.Orange);
            colors.Add(Color.Pink);
            colors.Add(Color.Purple);
            colors.Add(Color.Lime);
            colors.Add(Color.Olive);
            colors.Add(Color.Teal);

            return colors;
        }
    }
}
