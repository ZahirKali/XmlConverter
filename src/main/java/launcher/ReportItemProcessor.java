package launcher;

import org.springframework.batch.item.ItemProcessor;

public class ReportItemProcessor implements ItemProcessor<Report, Report>{
	@Override
	public Report process(Report item) throws Exception {
		System.err.println(item.toString());
		return item;
	}
}
