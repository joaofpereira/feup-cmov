using currency_converter.API;
using currency_converter.model;
using currency_converter.utils;
using System;
using System.Collections.Generic;
using Xamarin.Forms;

namespace currency_converter
{
    public partial class RatesPage : ContentPage
    {
        DataAccess db;
        HttpRequest httpRequest;
        Grid OutterGrid, InnerGrid;
        Picker currenciesPicker;
        List<CurrencyModel> originalCurrencies = new List<CurrencyModel>();
        List<CurrencyModel> managementCurrencies = new List<CurrencyModel>();
        int favouriteCurrency;

        public RatesPage()
        {
            Title = "Currency Rates List";

            httpRequest = new HttpRequest();

            db = DataAccess.GetDB;
            originalCurrencies = db.GetAllCurrency();

            CurrencyModel euroC = Utils.GetEuroCurrency(originalCurrencies);
            CurrencyModel mainC = Utils.GetMainCurrency(originalCurrencies);

            managementCurrencies = Utils.ConvertListTo(euroC, mainC, originalCurrencies);

            OutterGrid = new Grid()
            {
                RowSpacing = 1,
                ColumnSpacing = 1,
                HorizontalOptions = LayoutOptions.Center,
                VerticalOptions = LayoutOptions.CenterAndExpand
            };

            OutterGrid.RowDefinitions = new RowDefinitionCollection
            {
                new RowDefinition { Height = GridLength.Auto },
                new RowDefinition { Height = GridLength.Auto },
                new RowDefinition { Height = GridLength.Auto }
            };

            OutterGrid.ColumnDefinitions = new ColumnDefinitionCollection
            {
                    new ColumnDefinition { Width = new GridLength(4, GridUnitType.Star) }
            };

            UpdateInnerGrid();
            CreatePicker();

            Button setMainCurrency_btn = new Button();
            Device.OnPlatform(
                Android: () => setMainCurrency_btn = new Button()
                {
                    Text = "Set Main Currency",
                    Font = Font.SystemFontOfSize(NamedSize.Medium),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.FromHex("#3498DB"),
                    TextColor = Color.White,
                    WidthRequest = 120
                },
                WinPhone: () => setMainCurrency_btn = new Button()
                {
                    Text = "Set Main Currency",
                    Font = Font.SystemFontOfSize(NamedSize.Medium),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.White,
                    TextColor = Color.Black,
                    WidthRequest = 200
                }
            );
            setMainCurrency_btn.Clicked += OnSetMainCurrencyButtonClicked;

            OutterGrid.Children.Add(InnerGrid, 0, 0);
            OutterGrid.Children.Add(currenciesPicker, 0, 1);
            OutterGrid.Children.Add(setMainCurrency_btn, 0, 2);

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

            ToolbarItem walletToolbarItem = new ToolbarItem();
            Device.OnPlatform(
                Android: () => walletToolbarItem = new ToolbarItem()
                {
                    Text = "Wallet",
                    Order = ToolbarItemOrder.Primary
                },
                WinPhone: () => walletToolbarItem = new ToolbarItem()
                {
                    Text = "Wallet",
                    Order = ToolbarItemOrder.Secondary
                }
            );
            walletToolbarItem.Clicked += OnClickedWalletToolbarItem;

            ToolbarItems.Add(addRemoveToolbarItem);
            ToolbarItems.Add(walletToolbarItem);

            Content = new StackLayout
            {
                Children = { OutterGrid }
            };
        }

        private void UpdateInnerGrid()
        {
            InnerGrid = new Grid() {};

            InnerGrid.ColumnDefinitions = new ColumnDefinitionCollection
            {
                    new ColumnDefinition { Width = new GridLength(2, GridUnitType.Star) },
                    new ColumnDefinition { Width = new GridLength(2, GridUnitType.Star) }
            };

            InnerGrid.Children.Add(new Label()
            {
                Text = "Currency",
                FontAttributes = FontAttributes.Bold,
                FontSize = Device.GetNamedSize(NamedSize.Medium, typeof(Label)),
                HorizontalTextAlignment = TextAlignment.Center,
                TextColor = Color.FromHex("#3498DB")
            }, 0, 0);

            InnerGrid.Children.Add(new Label()
            {
                Text = "Rate",
                FontAttributes = FontAttributes.Bold,
                FontSize = Device.GetNamedSize(NamedSize.Medium, typeof(Label)),
                HorizontalTextAlignment = TextAlignment.Center,
                TextColor = Color.FromHex("#3498DB")
            }, 1, 0);

            int gridRow = 1;
            for (int i = 0; i < managementCurrencies.Count; i++)
            {
                Label currencyCode = new Label()
                {
                    Text = managementCurrencies[i].code,
                    HorizontalTextAlignment = TextAlignment.Center,
                    
                };
                Label currencyRate = new Label()
                {
                    Text = string.Format("{0:0.0000}", managementCurrencies[i].toCurrency),
                    HorizontalTextAlignment = TextAlignment.Center
                };
                if (managementCurrencies[i].isFavourite)
                {
                    favouriteCurrency = i;

                    currencyCode.TextColor = Color.White;
                    currencyCode.FontAttributes = FontAttributes.Bold;
                    currencyCode.BackgroundColor = Color.FromHex("#3498DB");

                    currencyRate.TextColor = Color.White;
                    currencyRate.FontAttributes = FontAttributes.Bold;
                    currencyRate.BackgroundColor = Color.FromHex("#3498DB");
                }

                InnerGrid.Children.Add(currencyCode, 0, gridRow);
                InnerGrid.Children.Add(currencyRate, 1, gridRow++);
            }
        }

        private void CreatePicker()
        {
            currenciesPicker = new Picker
            {
                Title = "Currency",
                WidthRequest = 90,
                HorizontalOptions = LayoutOptions.Center
            };

            for(int i = 0; i < managementCurrencies.Count; i++)
                currenciesPicker.Items.Add(managementCurrencies[i].code);

            currenciesPicker.SelectedIndex = favouriteCurrency;
            currenciesPicker.SelectedIndexChanged += CurrenciesPickerChanged;
        }

        private void CurrenciesPickerChanged(object sender, EventArgs e)
        {
            string code = currenciesPicker.Items[currenciesPicker.SelectedIndex];

            CurrencyModel currentCurrencySelected = managementCurrencies[currenciesPicker.SelectedIndex];
            CurrencyModel euroCurrency = Utils.GetEuroCurrency(managementCurrencies);

            managementCurrencies = Utils.ConvertListTo(euroCurrency, currentCurrencySelected, originalCurrencies);

            OutterGrid.Children.Remove(InnerGrid);
            UpdateInnerGrid();
            OutterGrid.Children.Add(InnerGrid, 0, 0);
        }

        private void OnSetMainCurrencyButtonClicked(object sender, EventArgs e)
        {
            CurrencyModel mainCurrency = originalCurrencies[favouriteCurrency];
            mainCurrency.isFavourite = false;
            DataAccess.GetDB.UpdateCurrency(mainCurrency);

            CurrencyModel newMainCurrency = originalCurrencies[currenciesPicker.SelectedIndex];
            newMainCurrency.isFavourite = true;
            DataAccess.GetDB.UpdateCurrency(newMainCurrency);

            originalCurrencies = DataAccess.GetDB.GetAllCurrency();

            CurrencyModel currentCurrencySelected = managementCurrencies[currenciesPicker.SelectedIndex];
            CurrencyModel euroCurrency = Utils.GetEuroCurrency(managementCurrencies);

            managementCurrencies = Utils.ConvertListTo(euroCurrency, currentCurrencySelected, originalCurrencies);

            OutterGrid.Children.Remove(InnerGrid);
            UpdateInnerGrid();
            OutterGrid.Children.Add(InnerGrid, 0, 0);
        }

        async void OnClickedAddRemoveToolbarItem(object sender, EventArgs e)
        {
            AddRemoveCurrencyPage newPage = new AddRemoveCurrencyPage();
            await Navigation.PushAsync(newPage, true);
        }

        async void OnClickedWalletToolbarItem(object sender, EventArgs e)
        {
            WalletPage newPage = new WalletPage();
            await Navigation.PushAsync(newPage, true);
        }
    }
}
