package com.test.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPag;
	private int numElementoPag;
	private int pagActual;
	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();

		this.numElementoPag = page.getSize();
		this.totalPag = page.getTotalPages();
		this.pagActual = page.getNumber() + 1;

		int desde, hasta;
		if (this.totalPag <= this.numElementoPag) {
			desde = 1;
			hasta = totalPag;
		} else {
			if (this.pagActual <= this.numElementoPag / 2) {
				desde = 1;
				hasta = this.numElementoPag;
			} else if (this.pagActual >= this.totalPag - this.numElementoPag / 2) {
				desde = this.totalPag - this.numElementoPag + 1;
				hasta = this.numElementoPag;
			} else {
				desde = this.pagActual - this.numElementoPag / 2;
				hasta = this.numElementoPag;
			}
		}
		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, pagActual == desde + i));
		}

	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	public String getUrl() {
		return url;
	}

	public Page<T> getPage() {
		return page;
	}

	public int getTotalPag() {
		return totalPag;
	}

	public int getNumElementoPag() {
		return numElementoPag;
	}

	public int getPagActual() {
		return pagActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

}
