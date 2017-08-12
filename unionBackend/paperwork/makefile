all:
	pdflatex main

release:
	pdflatex main
	bibtex main
	pdflatex main
	pdflatex main

clean: 
	rm -f \
		*.aux \
		*.bak \
		*.bbl \
		*.blg \
		*.dvi \
		*.glo \
		*.gls \
		*.idx \
		*.ilg \
		*.ind \
		*.ist \
		*.log \
		*.out \
		*.ps \
		*.thm \
		*.toc \
		*.lof \
		*.lot \
		*.loe \
		data/*.aux \
		dtx-style.sty
