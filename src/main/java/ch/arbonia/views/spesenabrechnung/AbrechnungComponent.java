package ch.arbonia.views.spesenabrechnung;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import ch.arbonia.data.Abrechnung;
import ch.arbonia.data.Ausgabe;
import ch.arbonia.data.Ausgabetyp;
import ch.arbonia.data.Gesellschaft;
import ch.arbonia.data.Kostenobjekt;
import ch.arbonia.data.Kostenobjektart;
import ch.arbonia.security.AuthenticatedUser;
import ch.arbonia.services.AbrechnungService;
import ch.arbonia.services.AusgabeService;
import ch.arbonia.services.UserService;

public class AbrechnungComponent extends Dialog {

   private final UserService userService;
   private final AbrechnungService abrechnungService;
   private final AusgabeService ausgabeService;
   private final AuthenticatedUser authenticatedUser;

   private TextField abrechnungsname;

   private ComboBox<Gesellschaft> gesellschaft;

   private ComboBox<Kostenobjektart> kostenobjektart;

   private ComboBox<Kostenobjekt> kostenobjekt;

   private ComboBox<Ausgabetyp> ausgabetyp;
   private DatePicker transaktionsdatum;
   private TextField geschaeftszweck;
   private TextField zahrungsart;
   private TextField waehrung;
   private TextField ausstellungsort;
   private NumberField betrag;

   private TextArea kommantar;

   private final Abrechnung abrechnung;

   public AbrechnungComponent(Abrechnung abrechnung, UserService userService, AbrechnungService abrechnungService, AusgabeService ausgabeService, AuthenticatedUser authenticatedUser) {
      this.abrechnung = abrechnung;
      this.userService = userService;
      this.abrechnungService = abrechnungService;
      this.ausgabeService = ausgabeService;
      this.authenticatedUser = authenticatedUser;

      this.abrechnungsname = new TextField("Abrechnungsname");

      this.gesellschaft = new ComboBox<>("Gesellschaft");
      gesellschaft.setItems(Gesellschaft.values());

      this.kostenobjektart = new ComboBox<>("Kostenobjektart");
      kostenobjektart.setItems(Kostenobjektart.values());

      this.kostenobjekt = new ComboBox<>("Kostenobjekt");
      kostenobjekt.setItems(Kostenobjekt.values());

      this.kommantar = new TextArea("Kommentar");
      kommantar.setWidthFull();

      this.ausgabetyp = new ComboBox<>("Ausgabetyp");
      ausgabetyp.setItems(Ausgabetyp.values());

      this.transaktionsdatum = new DatePicker("Transaktionsdatum");
      this.geschaeftszweck = new TextField("Geschäftszweck");
      this.zahrungsart = new TextField("Zahlungsart");
      this.waehrung = new TextField("Währung");
      this.ausstellungsort = new TextField("Ausstellungsort");
      this.betrag = new NumberField("Betrag");

      HorizontalLayout selects = new HorizontalLayout(abrechnungsname, gesellschaft, kostenobjektart, kostenobjekt);

      VerticalLayout abrechnungVL = new VerticalLayout(selects, kommantar);

      H3 ausgabenTitle = new H3("Ausgaben");

      HorizontalLayout transAndGeschHL = new HorizontalLayout(ausgabetyp, transaktionsdatum, geschaeftszweck, zahrungsart);

      HorizontalLayout zahWaeHL = new HorizontalLayout(ausstellungsort, waehrung, betrag);

      VerticalLayout ausgabenMain = new VerticalLayout();
      ausgabenMain.setVisible(false);

      MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
      Upload upload = new Upload(buffer);
      upload.setWidthFull();
      upload.setMaxFileSize(1);
      upload.addSucceededListener(event -> {
         Notification.show("Beleg hochgeladen");
      });

      H4 belegTitle = new H4("Beleg");

      VerticalLayout ausgabenCreateVL = new VerticalLayout(transAndGeschHL, zahWaeHL, belegTitle, upload);
      ausgabenCreateVL.setVisible(false);
      ausgabenCreateVL.setWidthFull();

      Grid<Ausgabe> ausgabeGrid = new Grid<>(Ausgabe.class, false);
      ausgabeGrid.setWidth("800px");
      ausgabeGrid.addColumn(Ausgabe::getAusgabetyp).setHeader("Ausgabentyp");
      ausgabeGrid.addColumn(Ausgabe::getTransaktionsdatumFormatted).setHeader("Transaktionsdatum");
      ausgabeGrid.addColumn(Ausgabe::getBetrag).setHeader("Betrag");

      Button newAusgabe = new Button("Neue Ausgabe");
      Button addAusgabe = new Button("Ausgabe hinzufügen");
      addAusgabe.setVisible(false);
      newAusgabe.addClickListener((event) -> {
         ausgabenCreateVL.setVisible(true);
         ausgabeGrid.setVisible(false);
         addAusgabe.setVisible(true);
         newAusgabe.setVisible(false);
      });
      Binder<Ausgabe> ausgabeBinder = new Binder<>(Ausgabe.class);
      ausgabeBinder.forField(ausgabetyp)
            .asRequired()
            .bind(Ausgabe::getAusgabetypByEnum, Ausgabe::setAusgabetypByEnum);
      ausgabeBinder.forField(transaktionsdatum)
            .asRequired()
            .bind(Ausgabe::getTransaktionsdatum, Ausgabe::setTransaktionsdatum);
      ausgabeBinder.forField(betrag)
            .asRequired()
            .bind(Ausgabe::getBetrag, Ausgabe::setBetrag);

      addAusgabe.addClickListener((event) -> {
         if (bindAusgabe(ausgabeBinder)) {
            newAusgabe.setVisible(true);
            addAusgabe.setVisible(false);
            ausgabeGrid.setVisible(true);
            ausgabenCreateVL.setVisible(false);
            ausgabeGrid.setItems(abrechnung.getAusgaben());

            ausgabetyp.clear();
            transaktionsdatum.clear();
            geschaeftszweck.clear();
            zahrungsart.clear();
            waehrung.clear();
            ausstellungsort.clear();
            betrag.clear();
         }
      });

      Binder<Abrechnung> abrechnungBinder = new Binder<>(Abrechnung.class);
      abrechnungBinder.forField(abrechnungsname)
            .asRequired()
            .bind(Abrechnung::getAbrechnungsname, Abrechnung::setAbrechnungsname);

      Button switchToAusgaben = new Button("Ausgaben erfassen");

      Div spacer2 = new Div();
      HorizontalLayout ausgabeButtons = new HorizontalLayout(spacer2, newAusgabe, addAusgabe);
      ausgabeButtons.setWidthFull();
      ausgabeButtons.expand(spacer2);

      Button abrechnungErstellenBtn = new Button("Speichern");
      abrechnungErstellenBtn.setVisible(false);
      abrechnungErstellenBtn.addClickListener(e -> {
         abrechnungService.save(abrechnung);
         close();
      });

      ausgabenMain.add(ausgabenTitle, ausgabenCreateVL, ausgabeGrid, ausgabeButtons);

      HorizontalLayout switchHL = new HorizontalLayout(switchToAusgaben);
      switchToAusgaben.addClickListener(e -> {
         if (bindAbrechnung(abrechnungBinder)) {
            abrechnungVL.setVisible(false);
            ausgabenMain.setVisible(true);
            switchHL.setVisible(false);
            abrechnungErstellenBtn.setVisible(true);
         }
      });
      Div spacer3 = new Div();
      switchHL.add(spacer3, switchToAusgaben);
      switchHL.expand(spacer3);

      add(abrechnungVL, switchHL, ausgabenMain);

      Button abbrechenBtn = new Button("Abbrechen");
      abbrechenBtn.addClickListener((e) -> close());

      Div spacer = new Div();
      HorizontalLayout footerButtons = new HorizontalLayout(abbrechenBtn, spacer, abrechnungErstellenBtn);
      footerButtons.setWidthFull();
      footerButtons.expand(spacer);

      getFooter().add(footerButtons);
      setHeaderTitle(getTitle());
      setResizable(true);
   }

   private String getTitle() {
      return abrechnung.getId() == null ? "Spesenabrechnung erstellen" : "Spesenabrechnung";
   }

   private boolean bindAbrechnung(Binder<Abrechnung> binder) {
      if (!binder.validate().isOk()) {
         return false;
      }
      try {
         abrechnung.setUser(authenticatedUser.get().get());
         binder.writeBean(abrechnung);
      } catch (ValidationException e) {
         Notification.show(e.getMessage());
      }

      return true;
   }

   private boolean bindAusgabe(Binder<Ausgabe> binder) {
      if (!binder.validate().isOk()) {
         return false;
      }
      try {
         Ausgabe ausgabe = new Ausgabe();
         ausgabe.setAbrechnung(abrechnung);
         abrechnung.getAusgaben().add(ausgabe);
         binder.writeBean(ausgabe);
      } catch (ValidationException e) {
         Notification.show(e.getMessage());
      }

      return true;
   }

}
