using currency_converter.API;
using currency_converter.model;
using System;
using System.Collections.Generic;
using Xamarin.Forms;

namespace currency_converter
{
    public partial class MainPage : ContentPage
    {
        HttpRequest httpRequest;
        List<CurrencyModel> currencies = new List<CurrencyModel>();
        DataAccess db;

        public MainPage()
        {
            db = DataAccess.GetDB;
            httpRequest = new HttpRequest();
            currencies = db.GetAllCurrency();

            Title = "Currency Converter";

            Button add_currency_btn = new Button();
            Device.OnPlatform(
                Android: () => add_currency_btn = new Button()
                {
                    Text = "Add/Remove Currency",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.FromHex("#3498DB"),
                    TextColor = Color.White,
                    Margin = new Thickness(0, 20, 0, 20)
                },
                WinPhone: () => add_currency_btn = new Button()
                {
                    Text = "Add/Remove Currency",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.White,
                    TextColor = Color.Black,
                    Margin = new Thickness(0, 20, 0, 20)
                }
            );
            add_currency_btn.Clicked += OnAddCurrencyButtonClicked;

            Button wallet_btn = new Button();
            Device.OnPlatform(
                Android: () => wallet_btn = new Button()
                {
                    Text = "View Wallet",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.FromHex("#3498DB"),
                    TextColor = Color.White,
                    Margin = new Thickness(0, 20, 0, 20)
                    
                },
                WinPhone: () => wallet_btn = new Button()
                {
                    Text = "View Wallet",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.White,
                    TextColor = Color.Black,
                    Margin = new Thickness(0, 20, 0, 20)
                }
            );
            wallet_btn.Clicked += OnWalletButtonClicked;

            Button rates_btn = new Button();
            Device.OnPlatform(
                Android: () => rates_btn = new Button()
                {
                    Text = "Rates",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.FromHex("#3498DB"),
                    TextColor = Color.White,
                    Margin = new Thickness(0, 20, 0, 20)

                },
                WinPhone: () => rates_btn = new Button()
                {
                    Text = "Rates",
                    Font = Font.SystemFontOfSize(NamedSize.Large),
                    BorderWidth = 1,
                    HorizontalOptions = LayoutOptions.Center,
                    VerticalOptions = LayoutOptions.Center,
                    BackgroundColor = Color.White,
                    TextColor = Color.Black,
                    Margin = new Thickness(0, 20, 0, 20)
                }
            );
            rates_btn.Clicked += OnRatesButtonClicked;

            BoxView divider = new BoxView();
            Device.OnPlatform(
                Android: () => {
                    divider = new BoxView() { Color = Color.FromHex("#3498DB"), WidthRequest = 100, HeightRequest = 2 };
                },
                WinPhone: () =>
                {
                    divider = new BoxView() { Color = Color.Black, WidthRequest = 100, HeightRequest = 2 };
                }
            );

            BoxView divider2 = new BoxView();
            Device.OnPlatform(
                Android: () => {
                    divider2 = new BoxView() { Color = Color.FromHex("#3498DB"), WidthRequest = 100, HeightRequest = 2 };
                },
                WinPhone: () =>
                {
                    divider2 = new BoxView() { Color = Color.Black, WidthRequest = 100, HeightRequest = 2 };
                }
            );

            ToolbarItem updateToolbarItem = new ToolbarItem();
            Device.OnPlatform(
                Android: () => updateToolbarItem = new ToolbarItem()
                {
                    Text = "Update Rates",
                    Order = ToolbarItemOrder.Primary
                },
                WinPhone: () => updateToolbarItem = new ToolbarItem()
                {
                    Text = "Update Rates",
                    Order = ToolbarItemOrder.Secondary
                }
            );
            updateToolbarItem.Clicked += OnClickedUpdateToolbarItem;

            ToolbarItems.Add(updateToolbarItem);

            // Build the page.
            Content = new StackLayout
            {
                Children =
                {
                    new StackLayout
                    {
                        VerticalOptions = LayoutOptions.CenterAndExpand,
                        Children =
                        {
                            add_currency_btn,
                            divider,
                            wallet_btn,
                            divider2,
                            rates_btn
                        }
                    }
                }
            };
        }

        private void OnClickedUpdateToolbarItem(object sender, EventArgs e)
        {
            httpRequest.RefreshRates(currencies, db);
        }

        async void OnAddCurrencyButtonClicked(object sender, EventArgs e)
        {
            AddRemoveCurrencyPage newPage = new AddRemoveCurrencyPage();
            await Navigation.PushAsync(newPage, true);
        }

        async void OnWalletButtonClicked(object sender, EventArgs e)
        {
            WalletPage newPage = new WalletPage();
            await Navigation.PushAsync(newPage, true);
        }

        async void OnRatesButtonClicked(object sender, EventArgs e)
        {
            RatesPage newPage = new RatesPage();
            await Navigation.PushAsync(newPage, true);
        }
    }
}
