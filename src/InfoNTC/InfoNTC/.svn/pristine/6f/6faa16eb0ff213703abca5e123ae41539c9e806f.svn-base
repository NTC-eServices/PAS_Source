package lk.informatics.ntc.view.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StartOfDayTaskExecutor implements Serializable, org.quartz.Job {

	public static Logger logger = Logger.getLogger(StartOfDayTaskExecutor.class);
	private static final long serialVersionUID = 1L;

	private void intializeStaticMaps() {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = ConnectionManager.getConnection();
		String sql = null;
		PreparedStatement ps = null;

		logger.info("intializeStaticMaps started");
		/** update nt_r_parameter table start **/
		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='INSPECTION_QUEUE_NUMBER_RUNNING_VALUES'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);

		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='RENEWAL_QUEUE_NUMBER_RUNNING_VALUES'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='AMMENDMENTS_QUEUE_NUMBER_RUNNING_VALUES'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);

		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='PAYMENT_QUEUE_NUMBER_RUNNING_VALUES'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='CANCEL_QUEUE_NUMBER_RUNNING_VALUES'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='NEW_PERMIT_QUEUE_NUMBER_RUNNING_VALUES'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='INQUIRY_QUEUE_NUMBER_RUNNING_VALUES'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='NEW_PERMIT_CALL_SKIP_QUEUE_NUMBER_VALUE'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='RENEWAL_CALL_SKIP_QUEUE_NUMBER_VALUE'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='AMMENDMENTS_CALL_SKIP_QUEUE_NUMBER_VALUE'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='PAYMENT_CALL_SKIP_QUEUE_NUMBER_VALUE'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='CANCEL_CALL_SKIP_QUEUE_NUMBER_VALUE'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='INSPECTION_CALL_SKIP_QUEUE_NUMBER_VALUE'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			sql = "UPDATE public.nt_r_parameters SET number_value=0 WHERE param_name='INQUIRY_CALL_SKIP_QUEUE_NUMBER_VALUE'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		/** update nt_r_parameter table end **/

		/** update nt_r_queue_number_run_value table start **/
		try {
			sql = "UPDATE public.nt_r_queue_number_run_value SET normal_current_no='1000' , priority_current_no='5000', normal_current_no_new='0', priority_current_no_new='500'; ";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		/** update nt_r_queue_number_run_value table end **/

		/** update nt_r_main_queue_number_run_value table start **/
		try {
			sql = "UPDATE public.nt_r_main_queue_number_run_value SET normal_current_no='000'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		/** update nt_r_main_queue_number_run_value table end **/

		/** update nt_r_counter table start **/
		try {
			sql = "UPDATE public.nt_r_counter SET cou_status='I' , cou_serving_queueno=NULL";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		/** update nt_r_counter table end **/

		/** update nt_r_main_counters table start **/
		try {
			sql = "UPDATE public.nt_r_main_counters SET cou_serving_queueno=NULL, cou_assigned_userid=NULL";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		/** update nt_r_main_counters table end **/

		/** update nt_t_main_queue_master table start **/
		try {

			sql = "UPDATE public.nt_t_main_queue_master SET status='C', end_date_time='" + timestamp
					+ "', completed_by='System' WHERE status !='C';";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		/** update nt_t_main_queue_master table end **/

		/** update nt_t_granted_user_role table - 03/09/2019 start **/
		try {

			sql = "update nt_t_granted_user_role set gur_status = 'I' where to_date(gur_end_date,'dd/MM/yyyy') <=  to_date(to_char(now(),'dd/MM/yyyy'),'dd/MM/yyyy') and gur_status='A' ";

			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ConnectionManager.close(ps);
		} catch (Exception e) {
			logger.error(e);
		}

		/** update nt_t_granted_user_role table - 03/09/2019 end **/
		try {

			ConnectionManager.close(ps);

			ConnectionManager.commit(con);

		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	public static void main(String[] args) {
		StartOfDayTaskExecutor s = new StartOfDayTaskExecutor();
		s.intializeStaticMaps();
	}

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.info("star of day job started");
		try {

			StartOfDayTaskExecutor s = new StartOfDayTaskExecutor();
			s.intializeStaticMaps();
			
			logger.info("star of day job finished");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("star of day execute error ");
		}

	}

}
