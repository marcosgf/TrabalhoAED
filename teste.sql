--
-- USDA Food Database R18
--
BEGIN;

SET client_encoding = 'LATIN1';

--
-- Name: data_src; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE data_src (
    datasrc_id character(6) NOT NULL,
    authors text,
    title text NOT NULL,
    "year" integer,
    journal text,
    vol_city text,
    issue_state text,
    start_page text,
    end_page text
);


--
-- Name: datsrcln; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE datsrcln (
    ndb_no character(5) NOT NULL,
    nutr_no character(3) NOT NULL,
    datasrc_id character(6) NOT NULL
);


--
-- Name: deriv_cd; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

COPY data_src (datasrc_id, authors, title, "year", journal, vol_city, issue_state, start_page, end_page) FROM stdin;
D1066 	G.V. Mann	The Health and Nutritional status of Alaskan Eskimos.	1962	American Journal of Clinical Nutrition	11		31	76
D1073 	J.P. McBride, R.A. Maclead	Sodium and potassium in fish from the Canadian Pacific coast.	1956	Journal of the American Dietetic Association	32		636	638
D1107 	M.E. Stansby	Chemical Characteristics of fish caught in the northwest Pacific Oceans.	1976	Marine Fish Rev.	38	9	1	11
D1191 	I.J. Tinsley, R.R. Lowry	Bromine content of lipids of marine organisms	1980	American Oil Chemists' Society, Journal		1	31	33
S101  	California Pistachio Commission	Nutrition Labeling Database for California Pistachios	1994					
S1023 	Produce Marketing Association (PMA)	Nutrient Content of Banana	1990					
S1024 	Produce Marketing Association (PMA)	Nutrient Content of Cantaloupe	1985					
S1026 	Produce Marketing Association (PMA)	Nutrient Content of Honeydew	1985					
S1027 	Produce Marketing Association (PMA)	Nutrient Content of Watermelon	1989					
S1031 	Produce Marketing Association (PMA)	Nutrient Content of Avocado	1989					
S1032 	Produce Marketing Association (PMA)	Nutrient Content of Date	1986					
S1033 	Produce Marketing Association (PMA)	Nutrient Content of Kiwi	1983					
S1041 	Nutrient Data Laboratory, ARS, USDA	Nutrient composition of Oscar Mayer Luncheon Meats	2002		Beltsville	MD		
S606  	Nutrient Data Laboratory, ARS, USDA	Variability of  the sugar content of foods	1989		Beltsville	MD		
S608  	Produce Marketing Association (PMA)	Nutrient Content of Tomato	1983					
S611  	Produce Marketing Association (PMA)	Nutrient Content of Pepper	1987					
S615  	Produce Marketing Association (PMA)	Nutrient Content of Eggplant	1991					
S616  	Produce Marketing Association (PMA)	Nutrient content of Broccoflower	1992					
S617  	Produce Marketing Association (PMA)	Nutrient Content of Radish	1982					
S618  	Produce Marketing Association (PMA)	Nutrient Content of Carrot	1982					
S619  	Produce Marketing Association (PMA)	Nutrient Content of Broccoli	1982					
S620  	Produce Marketing Association (PMA)	Nutrient Content of Lettuce	1991					
S622  	Produce Marketing Association (PMA)	Nutrient Content of Squash	1991					
S624  	Produce Marketing Association (PMA)	Nutrient Content of Lettuce	1982					
S625  	National Food Processors Association	NFPA Canned Vegetables	1993					
S626  	National Food Processors Association	NFPA Canned Vegetables	1995					
S627  	National Food Processors Association	NFPA Canned Vegetables	1994					
S641  	National Food Processors Association	NFPA Canned Vegetables	1992					
S642  	National Cancer Institute (NCI), DHHS	Total dietary fiber content of selected foods	1992					
S661  	Dahlgren & Company, Inc.	Sunflower analysis for Dahlgren & Company	1996		Crookston	MN		
S682  	Kellogg, Co.	Kellogg Company Data	2001					
S7    	Food and Drug Administration (FDA), DHHS	FDA Total Diet Study	1992						
\.

COPY datsrcln (ndb_no, nutr_no, datasrc_id) FROM stdin;
01026	203	S25   
01029	203	S27   
01032	203	S19   
01040	203	S27   
01046	203	S19   
01077	203	S27   
01079	203	S1063 
01079	203	S20   
01082	203	S1063 
01085	203	S1063 
01103	203	S1163 
01123	203	S1063 
01123	203	S561  
01124	203	S1065 
01125	203	S1065 
01202	203	S1163 
02048	203	S2109 
02053	203	S2109 
04017	203	S19   
04020	203	S19   
04021	203	S19   
04023	203	S19   
04053	203	S19   
04114	203	S19   
04120	203	S19   
04141	203	S19   
04142	203	S19   
04143	203	S19   
04144	203	S19   
04367	203	S19   
04610	203	S18   
04628	203	S18   
04629	203	S18   
04630	203	S19   
04631	203	S20   
04635	203	S19   
04636	203	S19   
04638	203	S26   
04639	203	S26   
04640	203	S26   
04641	203	S25   
04656	203	S2221 
04657	203	S2221 
04658	203	S2221 
04659	203	S2221 
04660	203	S2221 
04661	203	S2221 
04662	203	S2221 
04663	203	S2221 
04673	203	S2401 
04674	203	S2401 
04675	203	S2401 
04676	203	S2401 
04677	203	S2401 
05023	203	S1581 
05024	203	S1581 
05027	203	S1082 
05028	203	S1082 
05173	203	S1581 
05174	203	S1581 
05175	203	S1581 
05176	203	S1581 
05177	203	S1581 
05178	203	S1581 
05312	203	S26   
05313	203	S26   
05320	203	S26   
05623	203	D4282 
05624	203	S3    
05625	203	D4282 
05626	203	D4281 
05626	203	D4282 
05627	203	D4282 
05628	203	D4281 
05628	203	D4282 
05629	203	D4281 
05630	203	D4282 
05631	203	D4282 
05632	203	S3    
05643	203	D4301 
05644	203	D4301 
05645	203	D4301 
05646	203	D4301 
05647	203	D4301 
05648	203	D4301 
05649	203	D4301 
05650	203	D4301 
05651	203	D4301 
05652	203	D4301 
05653	203	D4301 
05654	203	D4301 
05655	203	D4301 
05656	203	D4301 
05657	203	D4301 
05658	203	D4301 
05661	203	S1082 
06016	203	S18   
06019	203	S18   
06043	203	S18   
06094	203	S18   
06128	203	S18   
06159	203	S18   
06164	203	S23   
06931	203	S22   
06982	203	S18   
06983	203	S18   
06984	203	S2104 
06985	203	S2104 
07002	203	S222  
07005	203	S248  
07006	203	S247  
07007	203	S1164 
07008	203	S238  
07011	203	S227  
07011	203	S281  
07013	203	S242  
07013	203	S281  
07014	203	S281  
07015	203	S248  
07016	203	S248  
07018	203	S226  
07021	203	S237  
07023	203	S242  
07027	203	S281  
07028	203	S225  
07028	203	S230  
07028	203	S281  
07029	203	S281  
07032	203	S281  
07034	203	S249  
07038	203	S281  
07039	203	S244  
07043	203	S230  
07052	203	S227  
07056	203	S248  
07057	203	S281  
07058	203	S237  
07063	203	S1164 
\.

--
-- Name: data_src_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY data_src
    ADD CONSTRAINT data_src_pkey PRIMARY KEY (datasrc_id);


--
-- Name: datsrcln_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY datsrcln
    ADD CONSTRAINT datsrcln_pkey PRIMARY KEY (ndb_no, nutr_no, datasrc_id);


--
-- Name: deriv_cd_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY deriv_cd
    ADD CONSTRAINT deriv_cd_pkey PRIMARY KEY (deriv_cd);


--
-- Name: fd_group_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY fd_group
    ADD CONSTRAINT fd_group_pkey PRIMARY KEY (fdgrp_cd);


--
-- Name: food_des_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY food_des
    ADD CONSTRAINT food_des_pkey PRIMARY KEY (ndb_no);


--
-- Name: nut_data_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY nut_data
    ADD CONSTRAINT nut_data_pkey PRIMARY KEY (ndb_no, nutr_no);


--
-- Name: nutr_def_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY nutr_def
    ADD CONSTRAINT nutr_def_pkey PRIMARY KEY (nutr_no);


--
-- Name: src_cd_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY src_cd
    ADD CONSTRAINT src_cd_pkey PRIMARY KEY (src_cd);


--
-- Name: weight_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY weight
    ADD CONSTRAINT weight_pkey PRIMARY KEY (ndb_no, seq);


--
-- Name: datsrcln_datasrc_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX datsrcln_datasrc_id_idx ON datsrcln USING btree (datasrc_id);


--
-- Name: food_des_fdgrp_cd_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX food_des_fdgrp_cd_idx ON food_des USING btree (fdgrp_cd);


--
-- Name: footnote_ndb_no_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX footnote_ndb_no_idx ON footnote USING btree (ndb_no, nutr_no);


--
-- Name: nut_data_deriv_cd_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX nut_data_deriv_cd_idx ON nut_data USING btree (deriv_cd);


--
-- Name: nut_data_nutr_no_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX nut_data_nutr_no_idx ON nut_data USING btree (nutr_no);


--
-- Name: nut_data_src_cd_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX nut_data_src_cd_idx ON nut_data USING btree (src_cd);


--
-- Name: datsrcln_datasrc_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY datsrcln
    ADD CONSTRAINT datsrcln_datasrc_id_fkey FOREIGN KEY (datasrc_id) REFERENCES data_src(datasrc_id);


--
-- Name: datsrcln_ndb_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY datsrcln
    ADD CONSTRAINT datsrcln_ndb_no_fkey FOREIGN KEY (ndb_no, nutr_no) REFERENCES nut_data(ndb_no, nutr_no);


--
-- Name: food_des_fdgrp_cd_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY food_des
    ADD CONSTRAINT food_des_fdgrp_cd_fkey FOREIGN KEY (fdgrp_cd) REFERENCES fd_group(fdgrp_cd);


--
-- Name: footnote_ndb_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY footnote
    ADD CONSTRAINT footnote_ndb_no_fkey FOREIGN KEY (ndb_no) REFERENCES food_des(ndb_no);


--
-- Name: footnote_nutr_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY footnote
    ADD CONSTRAINT footnote_nutr_no_fkey FOREIGN KEY (nutr_no) REFERENCES nutr_def(nutr_no);


--
-- Name: nut_data_deriv_cd_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY nut_data
    ADD CONSTRAINT nut_data_deriv_cd_fkey FOREIGN KEY (deriv_cd) REFERENCES deriv_cd(deriv_cd);


--
-- Name: nut_data_ndb_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY nut_data
    ADD CONSTRAINT nut_data_ndb_no_fkey FOREIGN KEY (ndb_no) REFERENCES food_des(ndb_no);


--
-- Name: nut_data_nutr_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY nut_data
    ADD CONSTRAINT nut_data_nutr_no_fkey FOREIGN KEY (nutr_no) REFERENCES nutr_def(nutr_no);


--
-- Name: nut_data_src_cd_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY nut_data
    ADD CONSTRAINT nut_data_src_cd_fkey FOREIGN KEY (src_cd) REFERENCES src_cd(src_cd);


--
-- Name: weight_ndb_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY weight
    ADD CONSTRAINT weight_ndb_no_fkey FOREIGN KEY (ndb_no) REFERENCES food_des(ndb_no);

