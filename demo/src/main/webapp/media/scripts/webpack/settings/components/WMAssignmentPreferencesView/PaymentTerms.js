/* eslint-disable react/require-default-props */
import PropTypes from 'prop-types';
import React from 'react';
import {
	WMRaisedButton,
	WMModal
} from '@workmarket/front-end-components';
import styles from './styles';

const PaymentTerms = ({
	open,
	handleClose
}) => (
	<WMModal
		title="Sample Assignment"
		open={ open }
		onRequestClose={ handleClose }
		actions={
			<WMRaisedButton
				primary
				label="Dismiss"
				onClick={ handleClose }
			/>
		}
		autoScrollBodyContent
	>
		<img
			src={ `${mediaPrefix}/images/settings/assignment.payment.terms.jpg` }
			style={ styles.modalContent }
			alt="Payment Terms Img"
		/>
	</WMModal>
);

PaymentTerms.propTypes = {
	open: PropTypes.bool,
	handleClose: PropTypes.func
};

export default PaymentTerms;
