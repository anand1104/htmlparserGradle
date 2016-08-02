package com.ahoy.parsetest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

public class AmazonParser {

	public static void main(String[] arg) {
		// HSSFWorkbook workbook = new HSSFWorkbook();
		FileWriter out = null;
		String[] cat = {
//		 "in_amazon_apparel",
		 "in_amazon_beauty"
//		 "in_amazon_books",
//				"in_amazon_ce"
//				"in_amazon_dvd"
//		 "in_amazon_home", "in_amazon_jewelry", "in_amazon_kitchen",
//		 "in_amazon_music", "in_amazon_shoes", "in_amazon_toys",
//		 "in_amazon_vg", "in_amazon_watches", "in_auto_all",
//		"in_generic_grocery","in_amazon_kindle","in_generic_kindle_ebooks",
//		 "in_generic_hpc", "in_generic_sports"
		};

		try {

			for (int a = 0; a < cat.length; a++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				String filePath = "F:/amazonxmls/data/" + sdf.format(new Date())
						+ "/" + cat[a] + "/";
				File f = new File(filePath);
				if (!f.exists())
					f.mkdirs();

				FileInputStream fin = new FileInputStream(
						"F:/amazonxmls/" + cat[a] + ".xml.gz");
				GZIPInputStream gzis = new GZIPInputStream(fin);
				InputStreamReader isr = new InputStreamReader(gzis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);

				String line = null;
				StringBuilder builder = new StringBuilder();
				int i = 0;
				int lineNo = 0;
				long itemCount = 0;
				String startTags = "";

				int totalCount = 0;

				int fileNo = 0;
				out = new FileWriter(new File(filePath + cat[a] + "_"
						+ (++fileNo) + ".txt"));

				out.write("item_unique_id | item_sku | parent_asin | item_mpn | item_brand | item_name | item_author | item_artist | item_model | item_category | item_short_desc | item_page_url | amzn_page_url | fm_page_url | offer_page_url | offer_used_url | tp_fba_url | item_image_url | item_image_url_small | item_image_url_large | item_salesrank | item_price | item_inventory | item_shipping_charge | item_merchant_id | fm_price | fm_inventory | fm_shipping_charge | fm_merchant_id | tp_new_price | tp_new_inventory | tp_new_shipping_charge | tp_new_merchant_id | color | size | department | savings_basis | list_price | tp_fba_price | tp_fba_inventory | tp_fba_shipping_charge | tp_fba_merchant_id | director | actor | artist | author | rating | os | isbn | format | merch_cat_name | merch_cat_path\n");

				while ((line = br.readLine()) != null) {
					if (lineNo < 2) {
						lineNo++;
						startTags += line + "\n";
						builder.append(line + "\n");

					} else {
						if (line.trim().contains("</item_data>")) {
							totalCount++;
							itemCount++;
							i++;
							if (itemCount == 1000) {
								builder.append(line + "\n</DataFeeds>");//
								if(totalCount>100000){
									processData(builder.toString(), out);
								}
								if (i == 100000) {									
									out.flush();
									out.close();
									out = new FileWriter(new File(filePath
											+ cat[a] + "_" + (++fileNo)
											+ ".txt"));
									System.out.println("Total COunt | cat | fileNo " + totalCount +" | "+cat[a]+" | "+fileNo);
									out.write("item_unique_id | item_sku | parent_asin | item_mpn | item_brand | item_name | item_author | item_artist | item_model | item_category | item_short_desc | item_page_url | amzn_page_url | fm_page_url | offer_page_url | offer_used_url | tp_fba_url | item_image_url | item_image_url_small | item_image_url_large | item_salesrank | item_price | item_inventory | item_shipping_charge | item_merchant_id | fm_price | fm_inventory | fm_shipping_charge | fm_merchant_id | tp_new_price | tp_new_inventory | tp_new_shipping_charge | tp_new_merchant_id | color | size | department | savings_basis | list_price | tp_fba_price | tp_fba_inventory | tp_fba_shipping_charge | tp_fba_merchant_id | director | actor | artist | author | rating | os | isbn | format | merch_cat_name | merch_cat_path\n");
									i = 0;
									
									if(totalCount>200000)break;
								}

								// System.out.println(builder.toString());

								builder = new StringBuilder();
								builder.append(startTags);
								itemCount = 0;
								// System.out.println(i);
								
							} else {
								builder.append(line + "\n");
							}
						} else {
							builder.append(line + "\n");
						}

					}
					// if(i==100){
					// break;
					// }
				}

				if (builder != null && !"".equals(builder.toString())) {
					processData(builder.toString(), out);
				}

				System.out.println("Total COunt | cat " + totalCount +" | "+cat[a]);

				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

	}

	// public static String getTagValue(String tagName, Element element) {
	// try {// element is single dealElement
	// NodeList nodeList = element.getElementsByTagName(tagName);//
	// .item(0).getChildNodes();
	// if (nodeList == null || nodeList.getLength() < 1) {
	// return null;
	// }
	// nodeList = nodeList.item(0).getChildNodes();
	// if (nodeList == null || nodeList.getLength() < 1) {
	// return null;
	// }
	// Node node = nodeList.item(0);
	// String value = node.getNodeValue();
	// if (value != null && !"".equals(value)) {
	// value = junkBoucer(value);
	// value = value.replaceAll("\\|", "").replaceAll("\n", " ");
	// }
	// return value;
	// } catch (Exception e) {
	//
	// return null;
	// }
	//
	// }

	public static void processData(String xml, FileWriter out) throws Exception {
		try {
			xml = xml.toString().replaceAll("&([^;]+(?!(?:\\w|;)))", "&amp;$1");
			xml = xml.replaceAll("&#29", "");
			// System.out.println("\n==============================="+xml);

			Document doc = Jsoup.parse(xml);
			org.jsoup.select.Elements eles = doc.select("item_data");
			for (Element ele : eles) {
				String item_unique_id = ele.getElementsByTag("item_unique_id")
						.hasText() ? ele.getElementsByTag("item_unique_id")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String item_sku = ele.getElementsByTag("item_sku").hasText() ? ele
						.getElementsByTag("item_sku").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String parent_asin = ele.getElementsByTag("parent_asin")
						.hasText() ? ele.getElementsByTag("parent_asin").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_mpn = ele.getElementsByTag("item_mpn").hasText() ? ele
						.getElementsByTag("item_mpn").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String item_brand = ele.getElementsByTag("item_brand")
						.hasText() ? ele.getElementsByTag("item_brand").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_name = ele.getElementsByTag("item_name").hasText() ? ele
						.getElementsByTag("item_name").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String item_author = ele.getElementsByTag("item_author")
						.hasText() ? ele.getElementsByTag("item_author").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_artist = ele.getElementsByTag("item_artist")
						.hasText() ? ele.getElementsByTag("item_artist").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_model = ele.getElementsByTag("item_model")
						.hasText() ? ele.getElementsByTag("item_model").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_category = ele.getElementsByTag("item_category")
						.hasText() ? ele.getElementsByTag("item_category")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String item_short_desc = ele
						.getElementsByTag("item_short_desc").hasText() ? ele
						.getElementsByTag("item_short_desc").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_page_url = ele.getElementsByTag("item_page_url")
						.hasText() ? ele.getElementsByTag("item_page_url")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String amzn_page_url = ele.getElementsByTag("amzn_page_url")
						.hasText() ? ele.getElementsByTag("amzn_page_url")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String fm_page_url = ele.getElementsByTag("fm_page_url")
						.hasText() ? ele.getElementsByTag("fm_page_url").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String offer_page_url = ele.getElementsByTag("offer_page_url")
						.hasText() ? ele.getElementsByTag("offer_page_url")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String offer_used_url = ele.getElementsByTag("offer_used_url")
						.hasText() ? ele.getElementsByTag("offer_used_url")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String tp_fba_url = ele.getElementsByTag("tp_fba_url")
						.hasText() ? ele.getElementsByTag("tp_fba_url").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_image_url = ele.getElementsByTag("item_image_url")
						.hasText() ? ele.getElementsByTag("item_image_url")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String item_image_url_small = ele.getElementsByTag(
						"item_image_url_small").hasText() ? ele
						.getElementsByTag("item_image_url_small").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_image_url_large = ele.getElementsByTag(
						"item_image_url_large").hasText() ? ele
						.getElementsByTag("item_image_url_large").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_salesrank = ele.getElementsByTag("item_salesrank")
						.hasText() ? ele.getElementsByTag("item_salesrank")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String item_price = ele.getElementsByTag("item_price")
						.hasText() ? ele.getElementsByTag("item_price").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_inventory = ele.getElementsByTag("item_inventory")
						.hasText() ? ele.getElementsByTag("item_inventory")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String item_shipping_charge = ele.getElementsByTag(
						"item_shipping_charge").hasText() ? ele
						.getElementsByTag("item_shipping_charge").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String item_merchant_id = ele.getElementsByTag(
						"item_merchant_id").hasText() ? ele
						.getElementsByTag("item_merchant_id").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String fm_price = ele.getElementsByTag("fm_price").hasText() ? ele
						.getElementsByTag("fm_price").text() : null;
				String fm_inventory = ele.getElementsByTag("fm_inventory")
						.hasText() ? ele.getElementsByTag("fm_inventory")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String fm_shipping_charge = ele.getElementsByTag(
						"fm_shipping_charge").hasText() ? ele
						.getElementsByTag("fm_shipping_charge").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String fm_merchant_id = ele.getElementsByTag("fm_merchant_id")
						.hasText() ? ele.getElementsByTag("fm_merchant_id")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String tp_new_price = ele.getElementsByTag("tp_new_price")
						.hasText() ? ele.getElementsByTag("tp_new_price")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String tp_new_inventory = ele.getElementsByTag(
						"tp_new_inventory").hasText() ? ele
						.getElementsByTag("tp_new_inventory").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String tp_new_shipping_charge = ele.getElementsByTag(
						"tp_new_shipping_charge").hasText() ? ele
						.getElementsByTag("tp_new_shipping_charge").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String tp_new_merchant_id = ele.getElementsByTag(
						"tp_new_merchant_id").hasText() ? ele.getElementsByTag(
						"tp_new_merchant_id").text() : null;
				String color = ele.getElementsByTag("color").hasText() ? ele
						.getElementsByTag("color").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String size = ele.getElementsByTag("size").hasText() ? ele
						.getElementsByTag("size").text().replaceAll("\\|", " ")
						.replaceAll("\n", " ") : null;
				String department = ele.getElementsByTag("department")
						.hasText() ? ele.getElementsByTag("department").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String savings_basis = ele.getElementsByTag("savings_basis")
						.hasText() ? ele.getElementsByTag("savings_basis")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String list_price = ele.getElementsByTag("list_price")
						.hasText() ? ele.getElementsByTag("list_price").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String tp_fba_price = ele.getElementsByTag("tp_fba_price")
						.hasText() ? ele.getElementsByTag("tp_fba_price")
						.text().replaceAll("\\|", " ").replaceAll("\n", " ")
						: null;
				String tp_fba_inventory = ele.getElementsByTag(
						"tp_fba_inventory").hasText() ? ele
						.getElementsByTag("tp_fba_inventory").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String tp_fba_shipping_charge = ele.getElementsByTag(
						"tp_fba_shipping_charge").hasText() ? ele
						.getElementsByTag("tp_fba_shipping_charge").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				String tp_fba_merchant_id = ele.getElementsByTag(
						"tp_fba_merchant_id").hasText() ? ele
						.getElementsByTag("tp_fba_merchant_id").text()
						.replaceAll("\\|", " ").replaceAll("\n", " ") : null;
				org.jsoup.select.Elements eles1 = ele.getElementsByTag("known_attr_val_pair");
				String isbn = null;
				String format = null;
				String director = null;
				String actor = null;
				String author = null;
				String os = null;
				String artist = null;
				String rating = null;
				for (Element ele1 : eles1) {

					if (ele1.attr("attr").contains("director")) {
						director = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					} else if (ele1.attr("attr").contains("isbn")) {
						isbn = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					} else if (ele1.attr("attr").contains("format")) {
						format = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					} else if (ele1.attr("attr").contains("author")) {
						author = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					} else if (ele1.attr("attr").contains("actor")) {
						actor = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					} else if (ele1.attr("attr").contains("artist")) {
						artist = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					} else if (ele1.attr("attr").contains("os")) {
						os = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					} else if (ele1.attr("attr").contains("rating")) {
						rating = ele1.attr("val").replaceAll("\\|", " ")
								.replaceAll("\n", " ");
					}
				}

				String merch_cat_name = ele.getElementsByTag(
						"merch_cat_name").hasText() ? ele.getElementsByTag(
						"merch_cat_name").text().replaceAll("\\|", " ")
						.replaceAll("\n", " ") : null;
				String merch_cat_path = ele.getElementsByTag(
						"merch_cat_path").hasText() ? ele.getElementsByTag(
						"merch_cat_path").text().replaceAll("\\|", " ")
						.replaceAll("\n", " ") : null;
				out.write(item_unique_id + " | " + item_sku + " | "
						+ parent_asin + " | " + item_mpn + " | " + item_brand
						+ " | " + item_name + " | " + item_author + " | "
						+ item_artist + " | " + item_model + " | "
						+ item_category + " | " + item_short_desc + " | "
						+ item_page_url + " | " + amzn_page_url + " | "
						+ fm_page_url + " | " + offer_page_url + " | "
						+ offer_used_url + " | " + tp_fba_url + " | "
						+ item_image_url + " | " + item_image_url_small + " | "
						+ item_image_url_large + " | " + item_salesrank + " | "
						+ item_price + " | " + item_inventory + " | "
						+ item_shipping_charge + " | " + item_merchant_id
						+ " | " + fm_price + " | " + fm_inventory + " | "
						+ fm_shipping_charge + " | " + fm_merchant_id + " | "
						+ tp_new_price + " | " + tp_new_inventory + " | "
						+ tp_new_shipping_charge + " | " + tp_new_merchant_id
						+ " | " + color + " | " + size + " | " + department
						+ " | " + list_price + " | " + savings_basis + " | "
						+ tp_fba_price + " | " + tp_fba_inventory + " | "
						+ tp_fba_shipping_charge + " | " + tp_fba_merchant_id
						+ " | " + director + " | " + actor + " | " + artist
						+ " | " + author + " | " + rating + " | " + os + " | "
						+ isbn + " | " + format + " | " + merch_cat_name
						+ " | " + merch_cat_path + "\n");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static String junkBoucer(String str) {
		StringBuilder builder = new StringBuilder();
		try {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) < 127) {
					builder.append(str.charAt(i));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return builder.toString();
	}
}
